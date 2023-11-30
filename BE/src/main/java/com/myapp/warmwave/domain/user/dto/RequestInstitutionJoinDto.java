package com.myapp.warmwave.domain.user.dto;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RequestInstitutionJoinDto {

    @NotBlank(message = "email은 필수 입력입니다.")
    @Email
    private String email;

    @NotBlank(message = "password는 필수 입력입니다.")
    private String password;

    @NotBlank(message = "기관이름은 필수 입력입니다.")
    private String institutionName;

    @NotBlank(message = "사업자 등록번호는 필수 입력입니다.")
    private String registerNum;

    // 전체 주소
    private String fullAddr;

    // 시,도
    private String sdName;

    // 시,군,구
    private String sggName;

    // 상세주소
    private String details;

    public Institution toEntity(PasswordEncoder passwordEncoder, Address address) {
        return Institution.builder()
                .email(getEmail())
                .password(passwordEncoder.encode(getPassword()))
                .registerNum(getRegisterNum())
                .institutionName(getInstitutionName())
                .address(address)
                .temperature(0F)
                .profileImg(UserService.DEFAULT_PROFILE_IMG_INST)
                .role(Role.INSTITUTION)
                .emailAuth(false)   // 이메일 인증여부 추가
                .isApprove(Boolean.FALSE)
                .build();
    }
}
