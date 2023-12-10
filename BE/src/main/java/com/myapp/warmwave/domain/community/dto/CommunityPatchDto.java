package com.myapp.warmwave.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPatchDto {

    private String title;

    private String contents;

    private String category;
}
