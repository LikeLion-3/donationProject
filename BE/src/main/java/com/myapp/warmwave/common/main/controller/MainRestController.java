package com.myapp.warmwave.common.main.controller;

import com.myapp.warmwave.common.main.dto.MainDto;
import com.myapp.warmwave.common.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainRestController {
    private final MainService mainService;

    @GetMapping("/count")
    public MainDto getCount() {
        return mainService.getInfo();
    }
}
