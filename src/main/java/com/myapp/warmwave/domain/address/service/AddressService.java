package com.myapp.warmwave.domain.address.service;

import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.address.repository.AddressRepository;
import com.myapp.warmwave.domain.user.dto.RequestInstitutionUpdateDto;
import com.myapp.warmwave.domain.user.entity.Institution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
    private final AddressRepository addressRepository;

    @Transactional
    public Address createAddress(String fullAddr, String sdName, String sggName, String details) {
        Address address = Address.builder()
                .fullAddr(fullAddr)
                .sdName(sdName)
                .sggName(sggName)
                .details(details)
                .build();

        return addressRepository.save(address);
    }

    public Optional<Address> findAddress(String fullAddr) {
        return addressRepository.findByFullAddr(fullAddr);
    }

    public void updateAddress(RequestInstitutionUpdateDto dto, Institution institution) {
        Address originalAddress = institution.getAddress();

        originalAddress.update(dto.getFullAddr(), dto.getSdName(), dto.getSggName(), dto.getDetails());
        addressRepository.save(originalAddress);
    }
}
