package com.myapp.warmwave.domain.user.dto;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RequestInstitutionJoinDto {
    private String email;

    private String password;

    private String institutionName;

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
                .isApprove(Boolean.FALSE)
                .build();
    }
}
