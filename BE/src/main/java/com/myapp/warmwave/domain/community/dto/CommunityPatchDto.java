package com.myapp.warmwave.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommunityPatchDto {

    private String title;

    private String contents;

    private String category;
}
