package com.myapp.warmwave.common.main.dto;

import lombok.Data;

@Data
public class MainInstDto {
    private Long instId;

    private String instName;

    private String fullAddr;

    private String sdName;

    private String sggName;

    private int donationCount;

    private int favoriteCount;
}
