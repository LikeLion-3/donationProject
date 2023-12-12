package com.myapp.warmwave.domain.favorite.service;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.favorite_inst.dto.FavoriteInstDto;
import com.myapp.warmwave.domain.favorite_inst.dto.ResponseFavoriteDto;
import com.myapp.warmwave.domain.favorite_inst.entity.FavoriteInst;
import com.myapp.warmwave.domain.favorite_inst.repository.FavoriteInstRepository;
import com.myapp.warmwave.domain.favorite_inst.service.FavoriteInstService;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteInstServiceTest {
    @Mock
    private FavoriteInstRepository favoriteInstRepository;

    @Mock
    private UserRepository<User> userRepository;

    @InjectMocks
    private FavoriteInstService favoriteInstService;

    private Individual individual;
    private Institution institution;
    private FavoriteInst favoriteInst;

    @BeforeEach
    void setup() {
        individual = Individual.builder()
                .id(1L)
                .email("test1@gmail.com")
                .nickname("닉네임1")
                .role(Role.INDIVIDUAL)
                .build();

        institution = Institution.builder()
                .id(2L)
                .email("test2@gmail.com")
                .institutionName("기관명1")
                .role(Role.INSTITUTION)
                .build();

        favoriteInst = FavoriteInst.builder()
                .id(1L)
                .individualUser(individual)
                .institutionUser(institution)
                .createdAt(LocalDateTime.of(2023, 12, 11, 12, 44))
                .build();
    }

    @DisplayName("관심 기관 등록 기능 확인")
    @Test
    void followInst() {
        // given
        String indivEmail = individual.getEmail();
        Long institutionId = institution.getId();

        when(userRepository.findByEmail(indivEmail)).thenReturn(Optional.of(individual));
        when(userRepository.findById(institutionId)).thenReturn(Optional.of(institution));

        when(favoriteInstRepository.save(any())).thenReturn(favoriteInst);

        // when
        ResponseFavoriteDto resDto = favoriteInstService.createFavoriteInst(institutionId, indivEmail);

        // then
        assertThat(resDto).isNotNull();
    }
    
    @DisplayName("관심 기관 목록 조회 기능 확인")
    @Test
    void readAll() {
        // given
        String indivEmail = individual.getEmail();
        Long institutionId = institution.getId();

        when(userRepository.findByEmail(indivEmail)).thenReturn(Optional.of(individual));
        when(userRepository.findById(institutionId)).thenReturn(Optional.of(institution));
        when(favoriteInstRepository.save(any())).thenReturn(favoriteInst);

        favoriteInstService.createFavoriteInst(institutionId, indivEmail);

        Long individualId = 1L;
        when(userRepository.findById(individualId)).thenReturn(Optional.of(individual));

        List<FavoriteInst> favoriteList = List.of(favoriteInst);
        when(favoriteInstRepository.findAllByIndividualUser(any())).thenReturn(favoriteList);
        
        // when
        List<FavoriteInstDto> resDtoList = favoriteInstService.findAllFavoriteInstByIndividual(individualId);
    
        // then
        assertThat(resDtoList).hasSameSizeAs(favoriteList);
    }

    @DisplayName("관심 기관 삭제 기능 확인")
    @Test
    void delete() {
        // given
        Long institutionId = institution.getId();
        String indivEmail = individual.getEmail();
        when(userRepository.findByEmail(indivEmail)).thenReturn(Optional.of(individual));
        when(userRepository.findById(institutionId)).thenReturn(Optional.of(institution));
        when(favoriteInstRepository.save(any())).thenReturn(favoriteInst);

        favoriteInstService.createFavoriteInst(institutionId, indivEmail);

        // when
        favoriteInstService.deleteFavoriteInst(institutionId, indivEmail);

        // then
        assertThat(favoriteInstRepository.count()).isZero();
    }
}
