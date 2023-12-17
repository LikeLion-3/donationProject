package com.myapp.warmwave.domain.user.service;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.jwt.JwtProvider;
import com.myapp.warmwave.common.main.dto.MainInstDto;
import com.myapp.warmwave.config.security.CookieManager;
import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.address.service.AddressService;
import com.myapp.warmwave.domain.email.dto.RequestEmailAuthDto;
import com.myapp.warmwave.domain.email.entity.EmailAuth;
import com.myapp.warmwave.domain.email.service.EmailService;
import com.myapp.warmwave.domain.user.dto.*;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.*;
import static com.myapp.warmwave.config.security.CookieManager.ACCESS_TOKEN;
import static com.myapp.warmwave.config.security.CookieManager.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository<User> userRepository;
    private final AddressService addressService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CookieManager cookieManager;

    public static final String DEFAULT_PROFILE_IMG_INST = "/static/profile/default_inst.jpg";
    public static final String DEFAULT_PROFILE_IMG_INDI = "/static/profile/default_indi.jpg";

    // 기관 회원가입
    @Transactional
    public ResponseUserJoinDto joinInstitution(RequestInstitutionJoinDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException(ALREADY_EXIST_USER);
        }

        Optional<Address> address = addressService.findAddress(dto.getFullAddr());

        if (address.isEmpty()) {
            address = Optional.of(addressService.createAddress(dto.getFullAddr(), dto.getSdName(), dto.getSggName(), dto.getDetails()));
        }

        // 이메일 인증 로직 추가
        EmailAuth emailAuth = emailService.createEmailAuth(dto.getEmail());

        Institution institution = dto.toEntity(passwordEncoder, address.get(), emailAuth);
        userRepository.save(institution);

        emailService.send(emailAuth.getEmail(), emailAuth.getAuthToken());
        return ResponseUserJoinDto.builder()
                .id(institution.getId())
                .email(institution.getEmail())
                .authToken(emailAuth.getAuthToken())
                .build();
    }

    // 개인 회원가입
    @Transactional
    public ResponseUserJoinDto joinIndividual(RequestIndividualJoinDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException(ALREADY_EXIST_USER);
        }

        EmailAuth emailAuth = emailService.createEmailAuth(dto.getEmail());

        Address address = addressService.createAddress(dto.getFullAddr(), dto.getSdName(), dto.getSggName(), dto.getDetails());

        Individual individual = dto.toEntity(passwordEncoder, address, emailAuth);

        userRepository.save(individual);

        emailService.send(emailAuth.getEmail(), emailAuth.getAuthToken());

        return ResponseUserJoinDto.builder()
                .id(individual.getId())
                .email(individual.getEmail())
                .authToken(emailAuth.getAuthToken())
                .build();
    }

    // 이메일 인증 성공
    @Transactional
    public void confirmEmail(RequestEmailAuthDto requestDto) {
        EmailAuth emailAuth = emailService.validEmail(requestDto.getEmail(), requestDto.getAuthToken(), LocalDateTime.now());

        User user = userRepository
                .findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        emailAuth.usedToken();
        user.getEmailAuth().emailVerified();
    }

    // 로그인
    @Transactional
    public ResponseUserLoginDto loginUser(HttpServletResponse response, RequestUserLoginDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_MATCH));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new CustomException(PASSWORD_NOT_MATCH);

        if (Boolean.FALSE.equals(user.getEmailAuth().getIsVerified()))
            throw new CustomException(EXPIRED_JWT);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtProvider.EMAIL_CLAIM, user.getEmail());

        String accessToken = jwtProvider.createAccessToken(claims);
        String refreshToken = jwtProvider.createRefreshToken();

        //pk, email, 사용자 이름(기관, 개인)
        Map<String, Object> cookieMap = new HashMap<>();
        cookieMap.put(ACCESS_TOKEN, accessToken);
        cookieMap.put(REFRESH_TOKEN, refreshToken);
        long expiration = (60 * 60 * 24 * 7);

        cookieManager.setCookie(response, cookieMap.get(ACCESS_TOKEN).toString(), ACCESS_TOKEN, expiration);
        cookieManager.setCookie(response, cookieMap.get(REFRESH_TOKEN).toString(), REFRESH_TOKEN, expiration);

        return new ResponseUserLoginDto(user.getId(), user.getEmail(), user.getName());
    }

    // 승인하지 않은 기관 회원 조회
    public List<ResponseUserDto> findAllByIsApproveFalse() {
        return userRepository.findAllByIsApproveFalseAndEmailAuthIsVerifiedTrue()
                .stream()
                .map(ResponseUserDto::fromEntity)
                .toList();
    }

    // 승인한 기관 회원 조회
    public List<ResponseUserDto> findAllByIsApproveTrue() {
        return userRepository.findAllByIsApproveTrueAndEmailAuthIsVerifiedTrue()
                .stream()
                .map(ResponseUserDto::fromEntity)
                .toList();
    }

    // 회원 조회
    public ResponseUserDto findUser(Long userId) {
        return userRepository.findById(userId)
                .map(ResponseUserDto::fromEntity).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }

    // 기관 단일 조회 -> 승인한 기관만 조회 가능
    public ResponseUserDto findInstitution(Long userId) {
        return userRepository.findById(userId)
                .map(Institution.class::cast)
                .map(ResponseUserDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("에러"));
    }

    // 개인 회원 단일 조회
    public ResponseUserDto findIndividual(Long userId) {
        return userRepository.findById(userId)
                .map(Individual.class::cast)
                .map(ResponseUserDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("에러"));
    }

    // 전체 기관 회원 조회
    public List<ResponseUserDto> findAllByRoleInstitution() {
        return userRepository.findAll()
                .stream()
                .map(Institution.class::cast)
                .map(ResponseUserDto::fromEntity)
                .toList();
    }

    // 전체 개인 회원 조회
    public List<ResponseUserDto> findAllByRoleIndividual() {
        return userRepository.findAll()
                .stream()
                .map(Individual.class::cast)
                .map(ResponseUserDto::fromEntity)
                .toList();
    }

    // 기관 회원 정보 수정
    @Transactional
    public Long updateInstInfo(RequestInstitutionUpdateDto dto, Long userId) {
        Institution savedInstitution = userRepository.findById(userId)
                .map(Institution.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        Address address = addressService.updateInstitutionAddress(dto, savedInstitution);

        savedInstitution.updateUserInfo(passwordEncoder.encode(dto.getPassword()), address);

        return userRepository.save(savedInstitution).getId();
    }

    // 개인 회원 정보 수정
    @Transactional
    public Long updateIndiInfo(RequestIndividualUpdateDto dto, Long userId) {

        Individual savedIndividual = userRepository.findById(userId)
                .map(Individual.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        Address address = addressService.updateIndividualAddress(dto, savedIndividual);

        savedIndividual.updateIndiInfo(dto.getNickname(), passwordEncoder.encode(dto.getPassword()), address);

        return userRepository.save(savedIndividual).getId();
    }

    // 기관 가입 승인
    @Transactional
    public void changeStatus(Long userId) {
        Institution savedUser = userRepository.findById(userId)
                .map(Institution.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        savedUser.approve();
        userRepository.save(savedUser);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        userRepository.delete(savedUser);
    }

    public Page<MainInstDto> findAllByLocation(String email, int num) {
        Individual individual = userRepository.findByEmail(email)
                .map(Individual.class::cast)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        Pageable pageable = PageRequest.of(num, 5);

        return userRepository.findAllByLocation(individual.getAddress().getSdName(), individual.getAddress().getSggName(), pageable);
    }

    public User whenSocialLogin(OAuth2User oAuth2User, String userEmail, String providerTypeCode) {
        Optional<User> optUser = userRepository.findByEmail(userEmail);

        if (optUser.isPresent()) return optUser.get();

        // optUser가 null 이면 Individual 객체를 생성하고 반환
        String username = "";
        String email = "";

        switch (providerTypeCode) {
            case "KAKAO" -> {
                Map<String, String> oAuth2UserProperties = oAuth2User.getAttribute("properties");
                Map<String, String> oAuth2UserKakaoAccount = oAuth2User.getAttribute("kakao_account");

                if (oAuth2UserProperties != null) {
                    username = oAuth2UserProperties.get("nickname");
                }

                if (oAuth2UserKakaoAccount != null) {
                    email = oAuth2UserKakaoAccount.get("email");
                }
            }
        }
        return Individual.builder()
                .role(Role.INDIVIDUAL)
                .nickname(username)
                .email(email)
                .build();
    }
}
