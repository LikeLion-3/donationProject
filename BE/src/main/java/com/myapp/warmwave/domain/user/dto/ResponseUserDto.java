package com.myapp.warmwave.domain.user.dto;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
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

    private Boolean isApproved;

    private String fullAddr;

    private int articleCount;

    private int favoriteCount;

    public static ResponseUserDto fromEntity(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .fullAddr(user.getAddress().getFullAddr())
                .temperature(user.getTemperature())
                .articleCount(user.getArticles().size())
                .favoriteCount(user.getFavoriteList().size())
                .build();
    }

    public static ResponseUserDto fromEntity(Institution institution) {
        return ResponseUserDto.builder()
                .id(institution.getId())
                .name(institution.getInstitutionName())
                .email(institution.getEmail())
                .role(institution.getRole())
                .fullAddr(institution.getAddress().getFullAddr())
                .isApproved(institution.getIsApprove())
                .temperature(institution.getTemperature())
                .articleCount(institution.getArticles().size())
                .favoriteCount(institution.getFavoriteList().size())
                .build();
    }

    public static ResponseUserDto fromEntity(Individual individual) {
        return ResponseUserDto.builder()
                .id(individual.getId())
                .name(individual.getNickname())
                .email(individual.getEmail())
                .role(individual.getRole())
                .fullAddr(individual.getAddress().getFullAddr())
                .temperature(individual.getTemperature())
                .articleCount(individual.getArticles().size())
                .favoriteCount(individual.getFavoriteList().size())
                .build();
    }
}
