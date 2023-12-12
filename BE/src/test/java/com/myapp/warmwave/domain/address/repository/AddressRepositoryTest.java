package com.myapp.warmwave.domain.address.repository;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.config.QuerydslConfig;
import com.myapp.warmwave.domain.address.entity.Address;
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
public class AddressRepositoryTest {
    @Autowired
    private AddressRepository addressRepository;

    private Address address() {
        return Address.builder()
                .id(1L)
                .userType(Role.INDIVIDUAL)
                .fullAddr("서울 강남구 삼성동 OOO 애플트리타워 2층")
                .sdName("서울")
                .sggName("강남구")
                .details("삼성동 OOO 애플트리타워 2층")
                .build();
    }

    // CREATE
    @DisplayName("주소 객체 생성")
    @Test
    void addAddress() {
        // given
        Address address = address();

        // when
        Address savedAddress = addressRepository.save(address);

        // then
        assertThat(address).isEqualTo(savedAddress);
        assertThat(address.getSdName()).isEqualTo(savedAddress.getSdName());
        assertThat(address.getSggName()).isEqualTo(savedAddress.getSggName());
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(addressRepository.count()).isEqualTo(1);
    }

    @DisplayName("전체 주소 읽어오기")
    @Test
    void findAllAddress() {
        // given
        addressRepository.save(address());

        // when
        List<Address> addressList = addressRepository.findAll();

        // then
        assertThat(addressList).hasSize(1);
    }

    @DisplayName("주소 단일 불러오기")
    @Test
    void findAddressOne() {
        // given
        Address address = addressRepository.save(address());

        // when
        Optional<Address> savedAddress = addressRepository.findByFullAddr(address.getFullAddr());

        // then
        assertThat(savedAddress).isPresent();
        assertThat(savedAddress.get()).isEqualTo(address);
    }

    @DisplayName("주소 변경")
    @Test
    void updateAddress() {
        // given
        Address address = addressRepository.save(address());
        String originalAddr = address.getFullAddr();

        // when
        address.update("서울 강남구 논현동 OOO XX타워 2층", "서울", "강남구", "논현동 OOO XX타워 2층");
        Address savedAddress = addressRepository.save(address);

        // then
        assertThat(savedAddress.getFullAddr()).isNotEqualTo(originalAddr);
    }

    @DisplayName("주소 삭제")
    @Test
    void deleteAddress() {
        // given
        Address address = addressRepository.save(address());

        // when
        addressRepository.delete(address);

        Optional<Address> foundAddress = addressRepository.findByFullAddr(address.getFullAddr());

        // then
        assertThat(foundAddress).isEmpty();
    }
}
