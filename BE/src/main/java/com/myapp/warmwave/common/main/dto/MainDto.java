package com.myapp.warmwave.common.main.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MainDto {
    private int indivCount;

    private int instCount;

    // totalArticleCountByTypeDonation;
    private int totalDonationCount;

    private List<MainInstDto> instDtoList = new ArrayList<>();

    // 최신글 5개
    private List<MainArticleDto> articleDtoList = new ArrayList<>();
}
