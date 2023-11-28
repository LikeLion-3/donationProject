package com.myapp.warmwave.domain.user.dto;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.user.entity.Institution;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResponseUserDto {
    private Long id;

    private String name;

    private String email;

    private Role role;

    private Float temperature;

    private int articleCount;

    private int favoriteCount;

    public static ResponseUserDto FromEntity(Institution institution) {
        return ResponseUserDto.builder()
                .id(institution.getId())
                .name(institution.getInstitutionName())
                .email(institution.getEmail())
                .role(institution.getRole())
                .temperature(institution.getTemperature())
                .articleCount(institution.getArticles().size())
                .favoriteCount(institution.getFavoriteList().size())
                .build();
    }
}
