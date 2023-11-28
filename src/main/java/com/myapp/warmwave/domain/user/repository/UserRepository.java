package com.myapp.warmwave.domain.user.repository;

import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    // 개인

    // 기관

    List<Institution> findAllByIsApproveTrue();
    List<Institution> findAllByIsApproveFalse();

    Optional<Institution> findByEmail(String email);

    Optional<Institution> findByIdAndIsApproveTrue(Long userId);
}
