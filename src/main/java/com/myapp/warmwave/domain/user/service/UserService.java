package com.myapp.warmwave.domain.user.service;

import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.address.service.AddressService;
import com.myapp.warmwave.domain.user.dto.RequestInstitutionJoinDto;
import com.myapp.warmwave.domain.user.dto.RequestInstitutionUpdateDto;
import com.myapp.warmwave.domain.user.dto.ResponseUserDto;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository<User> userRepository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    public static final String DEFAULT_PROFILE_IMG_INST = "/static/profile/default_inst.jpg";

    // 기관 회원가입
    @Transactional
    public Long joinInstitution(RequestInstitutionJoinDto dto) {
        Address address = addressService.findAddress(dto.getFullAddr());

        if (address == null) {
            address = addressService.createAddress(dto.getFullAddr(), dto.getSdName(), dto.getSggName(), dto.getDetails());
        }

        Institution institution = dto.toEntity(passwordEncoder, address);

        return userRepository.save(institution).getId();
    }

    // 승인하지 않은 기관 회원 조회
    public List<ResponseUserDto> findAllByIsApproveFalse() {
        return userRepository.findAllByIsApproveFalse()
                .stream()
                .map(ResponseUserDto::FromEntity)
                .collect(Collectors.toList());
    }

    // 승인한 기관 회원 조회
    public List<ResponseUserDto> findAllByIsApproveTrue() {
        return userRepository.findAllByIsApproveTrue()
                .stream()
                .map(ResponseUserDto::FromEntity)
                .collect(Collectors.toList());
    }

    // 기관 단일 조회 -> 승인한 기관만 조회 가능
    public ResponseUserDto findInstitution(Long userId) {
        return userRepository.findByIdAndIsApproveTrue(userId)
                .map(ResponseUserDto::FromEntity)
                .orElseThrow(() -> new IllegalArgumentException("에러"));
    }

    // 전체 기관 회원 조회
    public List<ResponseUserDto> findAllByRoleInstitution() {
        return userRepository.findAll()
                .stream()
                .map(user -> (Institution) user)
//                .filter(Institution::getIsApprove) -> 승인 여부에 따라 다른데 우선 전체로 기준 잡고 조회함.
                .map(ResponseUserDto::FromEntity)
                .collect(Collectors.toList());
    }

    // 기관 회원 정보 수정
    @Transactional
    public Long updateInfo(RequestInstitutionUpdateDto dto, Long userId) {
        Address address = addressService.findAddress(dto.getFullAddr());

        Institution savedInstitution = userRepository.findById(userId)
                .map(user -> (Institution) user)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        addressService.updateAddress(dto, savedInstitution);

        savedInstitution.updateUserInfo(passwordEncoder.encode(dto.getPassword()), address);
        return userRepository.save(savedInstitution).getId();
    }

    // 기관 가입 승인
    @Transactional
    public void changeStatus(Long userId) {
        Institution savedUser = userRepository.findById(userId)
                .map(user -> (Institution) user)
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
}
