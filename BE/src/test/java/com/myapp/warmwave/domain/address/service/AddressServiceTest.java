package com.myapp.warmwave.domain.address.service;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.address.repository.AddressRepository;
import com.myapp.warmwave.domain.user.dto.RequestIndividualUpdateDto;
import com.myapp.warmwave.domain.user.dto.RequestInstitutionUpdateDto;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address address1;
    private Address address2;
    private Individual individual;
    private Institution institution;

    @BeforeEach
    void setup() {
        address1 = Address.builder()
                .id(1L)
                .fullAddr("서울 강남구 XX동")
                .sdName("서울")
                .sggName("강남구")
                .details("XX동")
                .userType(Role.INDIVIDUAL)
                .build();

        address2 = Address.builder()
                .id(2L)
                .fullAddr("서울 강남구 테헤란로 123")
                .sdName("서울")
                .sggName("강남구")
                .details("테헤란로 123")
                .userType(Role.INSTITUTION)
                .build();

        individual = Individual.builder()
                .id(1L)
                .address(address1)
                .role(Role.INDIVIDUAL)
                .build();

        institution = Institution.builder()
                .id(2L)
                .address(address2)
                .role(Role.INSTITUTION)
                .build();
    }

    @DisplayName("주소 생성 기능 확인")
    @Test
    void createAddr() {
        // given
        String fullAddr = "서울 강남구 XX동";
        String sdName = "서울";
        String sggName = "강남구";
        String details = "XX동";

        when(addressRepository.save(any())).thenReturn(address1);

        // when
        Address savedAddr = addressService.createAddress(fullAddr, sdName, sggName, details);

        // then
        assertThat(savedAddr).isNotNull();
        assertThat(savedAddr.getFullAddr()).isEqualTo(fullAddr);
    }

    @DisplayName("주소 검색 기능 확인")
    @Test
    void findAddr() {
        // given
        String fullAddr = "서울 강남구 XX동";
        String sdName = "서울";
        String sggName = "강남구";
        String details = "XX동";
        
        when(addressRepository.save(any())).thenReturn(address1);
        Address savedAddr = addressService.createAddress(fullAddr, sdName, sggName, details);

        when(addressRepository.findByFullAddr(any())).thenReturn(Optional.of(address1));
        
        // when
        Optional<Address> foundAddr = addressService.findAddress(fullAddr);
    
        // then
        assertThat(foundAddr).isPresent();
        assertThat(foundAddr.get().getFullAddr()).isEqualTo(savedAddr.getFullAddr());
    }

    @DisplayName("주소 변경 기능 확인(개인)")
    @Test
    void updateAddrIndiv() {
        // given
        String fullAddr = "서울 강남구 XX동";
        String sdName = "서울";
        String sggName = "강남구";
        String details = "XX동";

        when(addressRepository.save(any())).thenReturn(address1);
        Address savedAddr = addressService.createAddress(fullAddr, sdName, sggName, details);
        String savedFullAddr = savedAddr.getFullAddr();

        RequestIndividualUpdateDto reqDto = new RequestIndividualUpdateDto(
                "", "", "서울 성북구 OO동", "서울", "성북구", "OO동"
        );
        address1.update(reqDto.getFullAddr(), reqDto.getSdName(), reqDto.getSggName(), reqDto.getDetails());

        // when
        Address changedAddr = addressService.updateIndividualAddress(reqDto, individual);

        // then
        assertThat(changedAddr.getFullAddr()).isNotEqualTo(savedFullAddr);
    }

    @DisplayName("주소 변경 기능 확인(기관)")
    @Test
    void updateAddrInst() {
        // given
        String fullAddr = "서울 강남구 테헤란로 123";
        String sdName = "서울";
        String sggName = "강남구";
        String details = "테헤란로 123";

        when(addressRepository.save(any())).thenReturn(address2);
        Address savedAddr = addressService.createAddress(fullAddr, sdName, sggName, details);
        String savedFullAddr = savedAddr.getFullAddr();

        RequestInstitutionUpdateDto reqDto = new RequestInstitutionUpdateDto(
                "", "서울 성북구 OO동", "서울", "성북구", "OO동"
        );
        address2.update(reqDto.getFullAddr(), reqDto.getSdName(), reqDto.getSggName(), reqDto.getDetails());

        // when
        Address changedAddr = addressService.updateInstitutionAddress(reqDto, institution);

        // then
        assertThat(changedAddr.getFullAddr()).isNotEqualTo(savedFullAddr);
    }
}
