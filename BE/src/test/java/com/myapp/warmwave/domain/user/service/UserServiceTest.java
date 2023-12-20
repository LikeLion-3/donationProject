package com.myapp.warmwave.domain.user.service;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.common.jwt.JwtProvider;
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
import com.myapp.warmwave.domain.user.repository.IndividualRepository;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository<User> userRepository;

    @Mock
    private IndividualRepository individualRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private CookieManager cookieManager;

    @InjectMocks
    private UserService userService;

    private EmailAuth emailAuth;
    private Address address1;
    private Address address2;
    private Individual individual;
    private Institution institution;

    @BeforeEach
    void setup() {
        emailAuth = EmailAuth.builder()
                .id(1L)
                .email("test@gmail.com")
                .authToken("1234")
                .expired(false)
                .isVerified(false)
                .build();

        address1 = Address.builder()
                .id(1L)
                .fullAddr("서울 강남구 테헤란로 123")
                .sdName("서울")
                .sggName("강남구")
                .details("테헤란로 123")
                .userType(Role.INDIVIDUAL)
                .build();

        address2 = Address.builder()
                .id(2L)
                .fullAddr("서울 강남구 테헤란로 123")
                .sdName("서울")
                .sggName("강남구")
                .details("테헤란로 123")
                .userType(Role.INSTITUTION)
                .build();

        individual = Individual.builder()
                .id(1L)
                .email("test@gmail.com")
                .password(passwordEncoder.encode("a1234567"))
                .nickname("닉네임")
                .address(address1)
                .temperature(0F)
                .profileImg(UserService.DEFAULT_PROFILE_IMG_INDI)
                .role(Role.INDIVIDUAL)
                .emailAuth(emailAuth)
                .articles(new ArrayList<>())
                .favoriteList(new ArrayList<>())
                .build();

        institution = Institution.builder()
                .id(2L)
                .email("test@gmail.com")
                .password(passwordEncoder.encode("a1234567"))
                .institutionName("기관명1")
                .address(address2)
                .temperature(0F)
                .profileImg(UserService.DEFAULT_PROFILE_IMG_INST)
                .registerNum("1234567890")
                .emailAuth(emailAuth)
                .role(Role.INSTITUTION)
                .articles(new ArrayList<>())
                .favoriteList(new ArrayList<>())
                .isApprove(false)
                .build();
    }
    
    @DisplayName("이메일 중복체크 기능 확인")
    @Test
    void checkUserDuplicate() {
        // given
        String email = "test@example.com";
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // when
        boolean result = userService.checkUserDuplicate(email);

        // then
        assertThat(result).isTrue();
    }
    
    @DisplayName("닉네임 중복체크 기능 확인")
    @Test
    void checkNicknameDuplicate() {
        // given
        String nickname = "닉네임";
        when(individualRepository.existsByNickname(anyString())).thenReturn(false);

        // when
        boolean result = userService.checkNicknameDuplicate(nickname);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("개인회원 회원가입 기능 확인")
    @Test
    void joinIndiv() {
        // given
        RequestIndividualJoinDto requestDto = saveIndiv();
        when(userService.checkUserDuplicate(anyString())).thenReturn(false);
        when(userService.checkNicknameDuplicate(anyString())).thenReturn(false);

        // when
        ResponseUserJoinDto responseDto = userService.joinIndividual(requestDto);

        // then
        assertThat(responseDto).isNotNull();
    }

    @DisplayName("기관회원 회원가입 기능 확인")
    @Test
    void joinInst() {
        // given
        RequestInstitutionJoinDto requestDto = saveInst();

        // when
        ResponseUserJoinDto responseDto = userService.joinInstitution(requestDto);

        // then
        assertThat(responseDto).isNotNull();
    }

    @DisplayName("이메일 인증 성공 기능 확인")
    @Test
    void confirmEmail() {
        // given
        RequestEmailAuthDto reqDto = new RequestEmailAuthDto("test@gmail.com", "1234");

        when(emailService.validEmail(any(), any(), any())).thenReturn(emailAuth);
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(individual));

        // when
        userService.confirmEmail(reqDto);

        // then
        assertThat(individual.getEmailAuth().getIsVerified()).isTrue();
        assertThat(individual.getEmailAuth().getExpired()).isTrue();
    }

    @DisplayName("로그인 기능 확인")
    @Test
    void login() {
        // given
        HttpServletResponse response = new MockHttpServletResponse();
        RequestUserLoginDto reqDto = new RequestUserLoginDto("test@gmail.com", "1234");
        individual.getEmailAuth().emailVerified();

        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(individual));

        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtProvider.createAccessToken(any())).thenReturn("123456789");
        when(jwtProvider.createRefreshToken()).thenReturn("987654321");

        // when
        ResponseUserLoginDto resDto = userService.loginUser(response, reqDto);

        // then
        assertThat(resDto).isNotNull();
        assertThat(resDto.getId()).isEqualTo(1L);
    }

    @DisplayName("개인 목록 조회 기능 확인")
    @Test
    void readAllIndiv() {
        // given
        RequestIndividualJoinDto requestDto = saveIndiv();
        userService.joinIndividual(requestDto); // save 된 상태

        List<User> userList = List.of(individual);

        when(userRepository.findAll()).thenReturn(userList);

        // when
        List<ResponseUserDto> resDtoList = userService.findAllByRoleIndividual();

        // then
        assertThat(resDtoList).hasSameSizeAs(userList);
        assertThat(resDtoList.get(0).getRole()).isEqualTo(Role.INDIVIDUAL);
    }

    @DisplayName("기관 목록 조회 기능 확인")
    @Test
    void readAllInst() {
        // given
        RequestInstitutionJoinDto requestDto = saveInst();
        userService.joinInstitution(requestDto); // save 된 상태

        List<User> userList = List.of(institution);

        when(userRepository.findAll()).thenReturn(userList);

        // when
        List<ResponseUserDto> resDtoList = userService.findAllByRoleInstitution();

        // then
        assertThat(resDtoList).hasSameSizeAs(userList);
        assertThat(resDtoList.get(0).getRole()).isEqualTo(Role.INSTITUTION);
    }

    @DisplayName("개인 단일 조회 기능 확인")
    @Test
    void readIndiv() {
        // given
        RequestIndividualJoinDto requestDto = saveIndiv();
        userService.joinIndividual(requestDto);

        Long userId = 1L;
        when(userRepository.findById(any())).thenReturn(Optional.of(individual));
        
        // when
        ResponseUserDto resDto = userService.findIndividual(userId);
    
        // then
        assertThat(resDto).isNotNull();
        assertThat(resDto.getId()).isEqualTo(userId);
        assertThat(resDto.getRole()).isEqualTo(Role.INDIVIDUAL);
    }

    @DisplayName("기관 단일 조회 기능 확인")
    @Test
    void readInst() {
        // given
        RequestInstitutionJoinDto requestDto = saveInst();
        userService.joinInstitution(requestDto);

        Long userId = 2L;
        when(userRepository.findById(any())).thenReturn(Optional.of(institution));

        // when
        ResponseUserDto resDto = userService.findInstitution(userId);

        // then
        assertThat(resDto).isNotNull();
        assertThat(resDto.getId()).isEqualTo(userId);
        assertThat(resDto.getRole()).isEqualTo(Role.INSTITUTION);
    }

    @DisplayName("유저 정보 수정 기능 확인 (개인)")
    @Test
    void updateIndiv() {
        // given
        RequestIndividualJoinDto reqJoinDto = saveIndiv();
        userService.joinIndividual(reqJoinDto);

        RequestIndividualUpdateDto updateDto = new RequestIndividualUpdateDto(
                "b1234567", "닉네임변경", "서울 성북구 OO동", "서울", "성북구", "OO동"
        );

        Long userId = 1L;
        address1.update(updateDto.getFullAddr(), updateDto.getSdName(), updateDto.getSggName(), updateDto.getDetails());

        when(userRepository.findById(any())).thenReturn(Optional.of(individual));
        when(addressService.updateIndividualAddress(any(), any())).thenReturn(address1);
        when(userService.checkNicknameDuplicate(anyString())).thenReturn(false);

        // when
        Long id = userService.updateIndiInfo(updateDto, userId);

        // then
        assertThat(id).isEqualTo(userId);
        assertThat(individual.getNickname()).isEqualTo(updateDto.getNickname());
        assertThat(individual.getAddress().getFullAddr()).isEqualTo(updateDto.getFullAddr());
    }

    @DisplayName("유저 정보 수정 기능 확인 (기관)")
    @Test
    void updateInst() {
        // given
        RequestInstitutionJoinDto reqJoinDto = saveInst();
        userService.joinInstitution(reqJoinDto);

        RequestInstitutionUpdateDto updateDto = new RequestInstitutionUpdateDto(
                "b1234567", "서울 성북구 OO동", "서울", "성북구", "OO동"
        );

        Long userId = 2L;
        address2.update(updateDto.getFullAddr(), updateDto.getSdName(), updateDto.getSggName(), updateDto.getDetails());

        when(userRepository.findById(any())).thenReturn(Optional.of(institution));
        when(addressService.updateInstitutionAddress(any(), any())).thenReturn(address2);

        // when
        Long id = userService.updateInstInfo(updateDto, userId);

        // then
        assertThat(id).isEqualTo(userId);
        assertThat(institution.getAddress().getFullAddr()).isEqualTo(updateDto.getFullAddr());
    }

    @DisplayName("기관 가입 승인 기능 확인")
    @Test
    void approve() {
        // given
        RequestInstitutionJoinDto reqJoinDto = saveInst();
        userService.joinInstitution(reqJoinDto);

        Long userId = 2L;

        when(userRepository.findById(any())).thenReturn(Optional.of(institution));

        // when
        userService.changeStatus(userId);

        // then
        assertThat(institution.getIsApprove()).isTrue();
    }

    @DisplayName("회원탈퇴 기능 확인")
    @Test
    void deleteUser() {
        // given
        RequestIndividualJoinDto reqJoinDto = saveIndiv();
        userService.joinIndividual(reqJoinDto);

        Long userId = 1L;
        when(userRepository.findById(any())).thenReturn(Optional.of(individual));

        // when
        userService.deleteUser(userId);

        // then
        assertThat(userRepository.count()).isZero();
    }

    // 개인 회원가입 과정 메서드화
    private RequestIndividualJoinDto saveIndiv() {
        RequestIndividualJoinDto requestDto = new RequestIndividualJoinDto(
                "test@gmail.com", "a1234567", "닉네임",
                "서울 강남구 테헤란로 123", "서울", "강남구",
                "테헤란로 123"
        );

        when(addressService.createAddress(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(address1);
        when(emailService.createEmailAuth(anyString())).thenReturn(emailAuth);
        when(passwordEncoder.encode(anyString())).thenReturn("1234");

        when(userRepository.save(any())).thenReturn(individual);
        return requestDto;
    }

    // 기관 회원가입 과정 메서드화
    private RequestInstitutionJoinDto saveInst() {
        RequestInstitutionJoinDto requestDto = new RequestInstitutionJoinDto(
                "test@gmail.com", "a1234567", "기관명1",
                "1234567890", "서울 강남구 테헤란로 123", "서울", "강남구",
                "테헤란로 123"
        );

        when(addressService.createAddress(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(address2);
        when(emailService.createEmailAuth(anyString())).thenReturn(emailAuth);
        when(passwordEncoder.encode(anyString())).thenReturn("1234");

        when(userRepository.save(any())).thenReturn(institution);
        return requestDto;
    }
}
