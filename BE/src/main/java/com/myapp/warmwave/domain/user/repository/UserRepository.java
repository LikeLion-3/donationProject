package com.myapp.warmwave.domain.user.repository;

import com.myapp.warmwave.common.main.dto.MainInstDto;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    // 공통
    Optional<User> findByEmail(String email);

    // 기관
    List<Institution> findAllByIsApproveTrue();

    List<Institution> findAllByIsApproveFalse();

    Optional<Institution> findByIdAndIsApproveTrue(Long userId);

    @Query("SELECT new com.myapp.warmwave.common.main.dto.MainInstDto(ins.id, ins.institutionName, ins.address.fullAddr, ins.address.sdName, ins.address.sggName, " +
            "(SELECT COUNT(a) FROM ins.articles a WHERE a.articleType = 'DONATION')) " +
            "FROM Institution ins " +
            "WHERE ins.address.sdName = :sdName AND ins.address.sggName = :sggName")
    Page<MainInstDto> findAllByLocation(@Param("sdName") String sdName, @Param("sggName") String sggName, Pageable pageable);
}
