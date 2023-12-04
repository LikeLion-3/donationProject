package com.myapp.warmwave.common.main.dto;

import lombok.Data;

@Data
public class MainDto {
    private int indivCount;// 전체 개인회원

    private int instCount;// 기관 전체 개수

    // totalArticleCountByTypeDonation;
    private int totalDonationCount; //전체 기부개수
}
