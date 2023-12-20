package com.myapp.warmwave.domain.user.dto;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.email.entity.EmailAuth;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestIndividualJoinDto {

    @NotBlank(message = "email은 필수 입력입니다.")
    @Email(message = "email 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "password는 필수 입력입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,16}$", message = "비밀번호는 대소문자 구분 없이 영문과 숫자 8~16자여야 합니다.")
    private String password;

    @NotBlank(message = "nickname은 필수 입력입니다.")
    @Pattern(regexp = "^[\uac00-\ud7a3]{2,12}$", message = "닉네임은 한글 두자 이상 열두자 이하여야 합니다.")
    private String nickname;

    // 전체 주소
    private String fullAddr;

    // 시,도
    private String sdName;

    // 시,군,구
    private String sggName;

    // 상세주소
    private String details;

    public Individual toEntity(PasswordEncoder passwordEncoder, Address address, EmailAuth emailAuth) {
        return Individual.builder()
                .email(getEmail())
                .password(passwordEncoder.encode(getPassword()))
                .nickname(getNickname())
                .address(address)
                .temperature(0F)
                .profileImg(UserService.DEFAULT_PROFILE_IMG_INDI)
                .role(Role.INDIVIDUAL)
                .emailAuth(emailAuth)   // 이메일 인증여부 추가
                .build();
    }
}
