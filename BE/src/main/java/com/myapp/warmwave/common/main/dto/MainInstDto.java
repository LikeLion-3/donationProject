package com.myapp.warmwave.common.main.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainInstDto {
    private Long id;

    private String institutionName;

    private String fullAddr;

    private String sdName;

    private String sggName;

    private int donationCount;

    public MainInstDto(Long id, String institutionName, String fullAddr, String sdName, String sggName, int donationCount) {
        this.id = id;
        this.institutionName = institutionName;
        this.fullAddr = fullAddr;
        this.sdName = sdName;
        this.sggName = sggName;
        this.donationCount = donationCount;
    }
}
