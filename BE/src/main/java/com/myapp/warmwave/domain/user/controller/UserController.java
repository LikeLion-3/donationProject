package com.myapp.warmwave.domain.user.controller;

import com.myapp.warmwave.common.main.dto.MainInstDto;
import com.myapp.warmwave.domain.email.dto.RequestEmailAuthDto;
import com.myapp.warmwave.domain.user.dto.*;
import com.myapp.warmwave.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;

    // 기관회원가입
    @PostMapping("/register/institution")
    public ResponseEntity<ResponseUserJoinDto> register(@RequestBody RequestInstitutionJoinDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.joinInstitution(dto));
    }

    // 개인회원가입
    @PostMapping("/register/individual")
    public ResponseEntity<ResponseUserJoinDto> signup(@Valid @RequestBody RequestIndividualJoinDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.joinIndividual(dto));
    }

    // 이메일 인증
    @GetMapping("/confirm-email")   //인증 URL이 담긴 이메일을 전송받은 사용자가 URL 클릭 시 해당 URI로 매핑
    public ResponseEntity<String> confirmEmail(@ModelAttribute RequestEmailAuthDto requestDto) {
        userService.confirmEmail(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("인증이 완료되었습니다.");
    }

    // 일반 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseUserLoginDto> login(@RequestBody RequestUserLoginDto requestDto) {
        ResponseUserLoginDto responseDto = userService.loginUser(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> redirectLoginPage() {
        Map<String, Object> map = new HashMap<>();
        map.put("result", "FAIL");
        map.put("msg", "로그인 페이지로 이동해주세요.");
        return ResponseEntity.status(HttpStatus.OK).body(map);
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

    // 단일 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDto> findUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findUser(userId));
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
        return ResponseEntity.ok(userService.updateInstInfo(dto, userId));
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

    // 접속한 유저의 주소 근방의 기관 조회
    @GetMapping("/adjacent")
    public ResponseEntity<Page<MainInstDto>> findAllInstByAdjLocation(
            Authentication authentication,
            @RequestParam(value = "num", defaultValue = "0") int num
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.findAllByLocation(email, num));
    }
}
