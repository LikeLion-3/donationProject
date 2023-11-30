package com.myapp.warmwave.user.repository;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.address.repository.AddressRepository;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository<User> userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Address savedAddress;

    private Institution institution() {
        return Institution.builder()
                .id(1L)
                .email("email")
                .password("1234")
                .role(Role.INSTITUTION)
                .address(savedAddress)
                .temperature(0F)
                .profileImg("이미지1")
                .isApprove(false)
                .institutionName("기관1")
                .registerNum("123456789")
                .build();
    }

    @BeforeEach
    void setup() {
        savedAddress = addressRepository.save(Address.builder()
                .id(1L)
                .userType(Role.INSTITUTION)
                .details("상세주소1")
                .sdName("서울")
                .sggName("강남구")
                .fullAddr("서울 강남구 삼성동")
                .build());
    }

    @DisplayName("기관 사용자 회원가입")
    @Test
    void addUser() {
        // given
        Institution user = institution();

        // when
        Institution savedUser = userRepository.save(user);

        // then
        assertThat(user).isEqualTo(savedUser);
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(savedUser.getId()).isNotNull();
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @DisplayName("기관 사용자 조회 (전체)")
    @Test
    void findAllUser() {
        // given
        userRepository.save(institution());

        // when
        List<Institution> institutionList = userRepository.findAll()
                .stream()
                .filter(user -> user instanceof Institution)
                .map(user -> (Institution) user)
                .toList();

        // then
        assertThat(institutionList.size()).isEqualTo(1);
    }

    @DisplayName("기관 사용자 전체 조회 (승인 O)")
    @Test
    void findUserByIsApproveTrue() {
        // given
        Institution savedUser = institution();

        // when
        savedUser.approve();
        userRepository.save(savedUser);

        List<Institution> institutionList = userRepository.findAllByIsApproveTrue();

        // then
        assertThat(institutionList.size()).isEqualTo(1);
    }

    @DisplayName("기관 사용자 전체 조회 (승인 X)")
    @Test
    void findUserByIsApproveFalse() {
        // given
        userRepository.save(institution());

        // when
        List<Institution> institutionList = userRepository.findAllByIsApproveFalse();

        // then
        assertThat(institutionList.size()).isEqualTo(1);
    }

    @DisplayName("기관 사용자 단일 조회 (존재 O -> 승인 O)")
    @Test
    void findUserApprove() {
        // given
        Institution user = institution();
        user.approve();
        Institution savedUser = userRepository.save(user);

        // when
        Optional<Institution> foundUser = userRepository.findByEmail(savedUser.getEmail())
                .map(Institution.class::cast);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(savedUser);
        assertThat(foundUser.get().getIsApprove()).isTrue();
    }

    @DisplayName("기관 사용자 단일 조회 (존재 O -> 승인 X)")
    @Test
    void findUserNotApprove() {
        // given
        Institution savedUser = userRepository.save(institution());

        // when
        Optional<Institution> foundUser = userRepository.findByEmail(savedUser.getEmail())
                .map(Institution.class::cast);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(savedUser);
        assertThat(foundUser.get().getIsApprove()).isFalse();
    }

    @DisplayName("기관 사용자 가입 승인")
    @Test
    void approveUser() {
        // given
        Institution savedUser = institution();

        // when
        savedUser.approve();
        Institution foundUser = userRepository.save(savedUser);

        // then
        assertThat(foundUser.getIsApprove()).isTrue();
    }

    @DisplayName("기관 사용자 정보 수정")
    @Test
    void updateUserInfo() {
        // given
        Institution savedUser = userRepository.save(institution());

        // when
        String originalPassword = savedUser.getPassword();
        String originalFullAddress = savedUser.getAddress().getFullAddr();

        savedUser.getAddress().update("서울 강남구 OO동", "서울", "강남구", "상세주소2");
        savedUser.updateUserInfo("12345", addressRepository.save(savedUser.getAddress()));

        Institution updatedUser = userRepository.save(savedUser);

        // then
        assertThat(updatedUser.getPassword()).isNotEqualTo(originalPassword);
        assertThat(updatedUser.getAddress().getFullAddr()).isNotEqualTo(originalFullAddress);
    }

    @DisplayName("기관 사용자 정보 삭제")
    @Test
    void deleteUser() {
        // given
        Institution savedUser = userRepository.save(institution());

        // when
        userRepository.delete(savedUser);

        Optional<Institution> foundUser = userRepository.findByEmail(savedUser.getEmail())
                .map(Institution.class::cast);

        // then
        assertThat(foundUser).isEmpty();
    }
}
