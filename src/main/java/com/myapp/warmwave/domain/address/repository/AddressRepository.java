package com.myapp.warmwave.domain.address.repository;

import com.myapp.warmwave.domain.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByFullAddr(String address);
}
