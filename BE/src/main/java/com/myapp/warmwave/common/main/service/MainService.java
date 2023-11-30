package com.myapp.warmwave.common.main.service;

import com.myapp.warmwave.common.main.dto.MainDto;
import com.myapp.warmwave.common.main.repo.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {
    private final MainRepository mainRepository;

    public MainDto getInfo() {
        return mainRepository.getInfo();
    }
}
