package com.myapp.warmwave.domain.user.controller;

import com.myapp.warmwave.domain.user.dto.*;
import com.myapp.warmwave.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;

    // 기관회원가입
    @PostMapping("/register/institution")
    public ResponseEntity<Long> register(@RequestBody RequestInstitutionJoinDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.joinInstitution(dto));
    }

    // 개인회원가입
    @PostMapping("/register/individual")
    public ResponseEntity<Long> signup(@Valid @RequestBody RequestIndividualJoinDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.joinIndividual(dto));
    }

    // 전체 기관 회원 조회
    @GetMapping("/institution")
    public ResponseEntity<List<ResponseUserDto>> findAllInstitution() {
        return ResponseEntity.ok(userService.findAllByRoleInstitution());
    }

    // 전체 개인 회원 조회
    @GetMapping("/individual")
    public ResponseEntity<List<ResponseUserDto>> findAllIndividual() {
        return ResponseEntity.ok(userService.findAllByRoleIndividual());
    }

    // 승인된 기관 리스트 조회
    @GetMapping("/approved")
    public ResponseEntity<List<ResponseUserDto>> findAllInstitutionByApproved() {
        return ResponseEntity.ok(userService.findAllByIsApproveTrue());
    }

    // 아직 승인되지 않은 기관 리스트 조회
    @GetMapping("/unapproved")
    public ResponseEntity<List<ResponseUserDto>> findAllInstitutionByUnapproved() {
        return ResponseEntity.ok(userService.findAllByIsApproveFalse());
    }

    // 기관 단일 조회
    @GetMapping("/{userId}/institution")
    public ResponseEntity<ResponseUserDto> findInstitution(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findInstitution(userId));
    }

    // 개인 회원 단일 조회
    @GetMapping("/{userId}/individual")
    public ResponseEntity<ResponseUserDto> findIndividual(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findIndividual(userId));
    }

    // 기관 회원 정보 수정
    @PutMapping("/{userId}/institution")
    public ResponseEntity<Long> updateUserInfo(
            @PathVariable("userId") Long userId,
            @RequestBody RequestInstitutionUpdateDto dto
    ) {
        return ResponseEntity.ok(userService.updateInfo(dto, userId));
    }

    // 개인 회원 정보 수정
    @PutMapping("/{userId}/individual")
    public ResponseEntity<Long> updateUserInfo(
            @PathVariable("userId") Long userId,
            @RequestBody RequestIndividualUpdateDto dto
    ) {
        return ResponseEntity.ok(userService.updateIndiInfo(dto, userId));
    }

    // 기관 가입 승인
    @PutMapping("/{userId}/approve")
    public ResponseEntity<Void> approveStatus(@PathVariable("userId") Long userId) {
        userService.changeStatus(userId);
        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}