package com.myapp.warmwave.domain.user.repository;

import com.myapp.warmwave.domain.user.entity.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualRepository extends JpaRepository<Individual, Long> {
}
