package com.myapp.warmwave.domain.favorite_inst.dto;

import com.myapp.warmwave.domain.favorite_inst.entity.FavoriteInst;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResponseFavoriteDto {
    private Long fromId;
    private Long toId;

    public static ResponseFavoriteDto fromEntity(FavoriteInst favoriteInst) {
        return ResponseFavoriteDto.builder()
                .fromId(favoriteInst.getIndividualUser().getId())
                .toId(favoriteInst.getInstitutionUser().getId())
                .build();
    }
}
