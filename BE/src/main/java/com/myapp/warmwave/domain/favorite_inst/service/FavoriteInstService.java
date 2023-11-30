package com.myapp.warmwave.domain.favorite_inst.service;

import com.myapp.warmwave.domain.favorite_inst.dto.FavoriteInstDto;
import com.myapp.warmwave.domain.favorite_inst.entity.FavoriteInst;
import com.myapp.warmwave.domain.favorite_inst.repository.FavoriteInstRepository;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteInstService {
    private final FavoriteInstRepository favoriteInstRepository;

    private final UserRepository<User> userRepository;

    @Transactional
    public void createFavoriteInst(Long institutionId, String indivEmail) {
        Individual individual = userRepository.findByEmail(indivEmail)
                .map(Individual.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        Institution institution = userRepository.findById(institutionId)
                .map(Institution.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        // 자기 자신을 관심기관에 등록할 수 없다.
        if (individual.getId().equals(institutionId)) throw new IllegalArgumentException("자기 자신 팔로우 X");

        // 기관은 팔로우 할 수 없다.
        if (indivEmail.equals(institution.getEmail())) throw new IllegalArgumentException("기관 팔로우 X");

        FavoriteInst favoriteInst = FavoriteInst.builder()
                .individualUser(individual)
                .institutionUser(institution)
                .createdAt(LocalDateTime.now())
                .build();

        favoriteInstRepository.save(favoriteInst);
    }

    public List<FavoriteInstDto> findAllFavoriteInstByIndividual(Long individualId) {
        Individual individual = userRepository.findById(individualId)
                .map(Individual.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("개인 유저 찾기 에러"));

        return favoriteInstRepository.findAllByIndividualUser(individual)
                .stream()
                .map(FavoriteInstDto::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteFavoriteInst(Long institutionId, String indivEmail) {
        Individual individual = userRepository.findByEmail(indivEmail)
                .map(Individual.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        Institution institution = userRepository.findById(institutionId)
                .map(Institution.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("에러"));

        favoriteInstRepository.deleteByInstitutionUserAndIndividualUser(institution, individual);
    }
}
