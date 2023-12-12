package com.myapp.warmwave.domain.user.repository;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.config.QuerydslConfig;
import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.address.repository.AddressRepository;
import com.myapp.warmwave.domain.email.entity.EmailAuth;
import com.myapp.warmwave.domain.email.repository.EmailAuthRepository;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository<User> userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    private Address savedAddress1;
    private Address savedAddress2;
    private EmailAuth emailAuth1;
    private EmailAuth emailAuth2;

    private Individual individual() {
        return Individual.builder()
                .id(1L)
                .email("email1")
                .password("1234")
                .role(Role.INDIVIDUAL)
                .address(savedAddress1)
                .temperature(0F)
                .profileImg("이미지1")
                .emailAuth(emailAuth1)
                .nickname("닉네임1")
                .build();
    }

    private Institution institution() {
        return Institution.builder()
                .id(2L)
                .email("email2")
                .password("12345")
                .role(Role.INSTITUTION)
                .address(savedAddress2)
                .temperature(0F)
                .profileImg("이미지2")
                .isApprove(false)
                .institutionName("기관1")
                .emailAuth(emailAuth2)
                .registerNum("123456789")
                .build();
    }

    @BeforeEach
    void setup() {
        emailAuth1 = emailAuthRepository.save(EmailAuth.builder()
                .id(1L)
                .email(individual().getEmail())
                .authToken("9876")
                .isVerified(false)
                .expired(false)
                .build());

        emailAuth2 = emailAuthRepository.save(EmailAuth.builder()
                .id(2L)
                .email(institution().getEmail())
                .authToken("5432")
                .isVerified(false)
                .expired(false)
                .build());

        savedAddress1 = addressRepository.save(Address.builder()
                .id(1L)
                .userType(Role.INDIVIDUAL)
                .details("상세주소1")
                .sdName("서울")
                .sggName("강남구")
                .fullAddr("서울 강남구 삼성동")
                .build());

        savedAddress2 = addressRepository.save(Address.builder()
                .id(2L)
                .userType(Role.INSTITUTION)
                .details("상세주소2")
                .sdName("서울")
                .sggName("강남구")
                .fullAddr("서울 강남구 어딘가")
                .build());
    }

    // CREATE
    @DisplayName("개인 사용자 회원가입")
    @Test
    void addIndividualUser() {
        // given
        Individual user = individual();

        // when
        Individual savedUser = userRepository.save(user);

        // then
        assertThat(user).isEqualTo(savedUser);
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(savedUser.getId()).isNotNull();
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @DisplayName("기관 사용자 회원가입")
    @Test
    void addInstitutionUser() {
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

    // READ
    @DisplayName("개인 사용자 전체 조회 (조건 X)")
    @Test
    void findAllIndividualUser() {
        // given
        userRepository.save(individual());

        // when
        List<Individual> individualList = userRepository.findAll()
                .stream()
                .map(Individual.class::cast)
                .toList();

        // then
        assertThat(individualList).hasSize(1);
    }

    @DisplayName("기관 사용자 전체 조회 (조건 X)")
    @Test
    void findAllInstitutionUser() {
        // given
        userRepository.save(institution());

        // when
        List<Institution> institutionList = userRepository.findAll()
                .stream()
                .map(Institution.class::cast)
                .toList();

        // then
        assertThat(institutionList).hasSize(1);
    }

    @DisplayName("개인 사용자 전체 조회 (이메일 인증 O)")
    @Test
    void findAllIndividualUserByIsApproveTrue() {
        // given
        Individual savedUser = individual();

        // when
        savedUser.getEmailAuth().emailVerified();
        userRepository.save(savedUser);

        List<Individual> individualList = userRepository.findAllByEmailAuthIsVerifiedTrue();

        // then
        assertThat(individualList).hasSize(1);
    }

    @DisplayName("기관 사용자 전체 조회 (이메일 인증 O + 승인 O)")
    @Test
    void findAllInstitutionUserByEmailAuthTrueIsApproveTrue() {
        // given
        Institution savedUser = institution();

        // when
        savedUser.getEmailAuth().emailVerified();
        savedUser.approve();
        userRepository.save(savedUser);

        List<Institution> institutionList = userRepository.findAllByIsApproveTrueAndEmailAuthIsVerifiedTrue();

        // then
        assertThat(institutionList).hasSize(1);
    }

    @DisplayName("기관 사용자 전체 조회 (이메일 인증 O + 승인 X)")
    @Test
    void findAllInstitutionUserByEmailAuthTrueIsApproveFalse() {
        // given
        Institution savedUser = institution();

        // when
        savedUser.getEmailAuth().emailVerified();
        userRepository.save(savedUser);
        List<Institution> institutionList = userRepository.findAllByIsApproveFalseAndEmailAuthIsVerifiedTrue();

        // then
        assertThat(institutionList).hasSize(1);
    }

    @DisplayName("기관 사용자 전체 조회 (이메일 인증 X + 승인 X)")
    @Test
    void findInstitutionUserByEmailAuthFalseIsApproveFalse() {
        // given
        userRepository.save(institution());

        // when
        List<Institution> institutionList = userRepository.findAllByIsApproveFalseAndEmailAuthIsVerifiedFalse();

        // then
        assertThat(institutionList).hasSize(1);
    }

    @DisplayName("기관 사용자 단일 조회 (이메일 인증 O + 승인 O)")
    @Test
    void findInstitutionUserApprove() {
        // given
        Institution user = institution();
        user.getEmailAuth().emailVerified();
        user.approve();
        Institution savedUser = userRepository.save(user);

        // when
        Optional<Institution> foundUser = userRepository.findByEmail(savedUser.getEmail())
                .map(Institution.class::cast);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(savedUser);
        assertThat(foundUser.get().getEmailAuth().getIsVerified()).isTrue();
        assertThat(foundUser.get().getIsApprove()).isTrue();
    }

    @DisplayName("기관 사용자 단일 조회 (이메일 인증 O + 승인 X)")
    @Test
    void findInstitutionUserApproveAndEmailAuthFalse() {
        // given
        Institution user = institution();
        user.getEmailAuth().emailVerified();
        Institution savedUser = userRepository.save(user);

        // when
        Optional<Institution> foundUser = userRepository.findByEmail(savedUser.getEmail())
                .map(Institution.class::cast);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(savedUser);
        assertThat(foundUser.get().getEmailAuth().getIsVerified()).isTrue();
        assertThat(foundUser.get().getIsApprove()).isFalse();
    }

    @DisplayName("기관 사용자 단일 조회 (이메일 인증 X + 승인 X)")
    @Test
    void findInstitutionUserNotApprove() {
        // given
        Institution savedUser = userRepository.save(institution());

        // when
        Optional<Institution> foundUser = userRepository.findByEmail(savedUser.getEmail())
                .map(Institution.class::cast);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(savedUser);
        assertThat(foundUser.get().getEmailAuth().getIsVerified()).isFalse();
        assertThat(foundUser.get().getIsApprove()).isFalse();
    }

    // UPDATE
    @DisplayName("이메일 인증")
    @Test
    void emailVerified() {
        // given
        Individual individual = individual();
        Institution institution = institution();

        // when
        individual.getEmailAuth().emailVerified();
        institution.getEmailAuth().emailVerified();

        userRepository.save(individual);
        userRepository.save(institution);

        // then
        assertThat(individual.getEmailAuth().getIsVerified()).isTrue();
        assertThat(institution.getEmailAuth().getIsVerified()).isTrue();
    }

    @DisplayName("기관 사용자 가입 승인")
    @Test
    void approveInstitutionUser() {
        // given
        Institution savedUser = institution();

        // when
        savedUser.approve();
        Institution foundUser = userRepository.save(savedUser);

        // then
        assertThat(foundUser.getIsApprove()).isTrue();
    }

    @DisplayName("개인 사용자 정보 수정")
    @Test
    void updateIndividualUserInfo() {
        // given
        Individual savedUser = userRepository.save(individual());

        // when
        String originalPassword = savedUser.getPassword();
        String originalFullAddress = savedUser.getAddress().getFullAddr();
        String originalNickname = savedUser.getNickname();

        savedUser.getAddress().update("서울 성북구 OO동", "서울", "성북구", "상세주소3");
        savedUser.updateIndiInfo("변경된 닉네임", "12341234", addressRepository.save(savedUser.getAddress()));

        Individual updateIndividual = userRepository.save(savedUser);

        // then
        assertThat(updateIndividual.getPassword()).isNotEqualTo(originalPassword);
        assertThat(updateIndividual.getAddress().getFullAddr()).isNotEqualTo(originalFullAddress);
        assertThat(updateIndividual.getNickname()).isNotEqualTo(originalNickname);
    }

    @DisplayName("기관 사용자 정보 수정")
    @Test
    void updateInstitutionUserInfo() {
        // given
        Institution savedUser = userRepository.save(institution());

        // when
        String originalPassword = savedUser.getPassword();
        String originalFullAddress = savedUser.getAddress().getFullAddr();

        savedUser.getAddress().update("서울 강남구 OO동", "서울", "강남구", "상세주소4");
        savedUser.updateUserInfo("123456", addressRepository.save(savedUser.getAddress()));

        Institution updatedInstitution = userRepository.save(savedUser);

        // then
        assertThat(updatedInstitution.getPassword()).isNotEqualTo(originalPassword);
        assertThat(updatedInstitution.getAddress().getFullAddr()).isNotEqualTo(originalFullAddress);
    }

    // DELETE
    @DisplayName("회원 탈퇴")
    @Test
    void deleteInstitutionUser() {
        // given
        Individual savedIndividual = userRepository.save(individual());
        Institution savedInstitution = userRepository.save(institution());

        // when
        userRepository.delete(savedIndividual);
        userRepository.delete(savedInstitution);

        Optional<Individual> foundIndividual = userRepository.findByEmail(savedIndividual.getEmail())
                .map(Individual.class::cast);

        Optional<Institution> foundInstitution = userRepository.findByEmail(savedInstitution.getEmail())
                .map(Institution.class::cast);

        // then
        assertThat(foundIndividual).isEmpty();
        assertThat(foundInstitution).isEmpty();
    }
}
