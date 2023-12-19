package com.myapp.warmwave.domain.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CacheUserDto {
    private Long id;
    private String name;

    public CacheUserDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}