package com.myapp.warmwave.domain.favorite_inst.dto;

import com.myapp.warmwave.domain.favorite_inst.entity.FavoriteInst;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FavoriteInstDto {
    private Long id;

    private String institutionName;

    private String createdAt;

    public static FavoriteInstDto fromEntity(FavoriteInst favoriteInst) {
        return FavoriteInstDto.builder()
                .id(favoriteInst.getId())
                .institutionName(favoriteInst.getInstitutionUser().getInstitutionName())
                .createdAt(favoriteInst.getCreatedAt().toString())
                .build();
    }
}
