package com.myapp.warmwave.domain.community.repository;

import com.myapp.warmwave.domain.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityListRepository {
    Optional<Community> findByTitle(String title);
}
