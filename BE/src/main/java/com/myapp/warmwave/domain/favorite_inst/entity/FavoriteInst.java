package com.myapp.warmwave.domain.favorite_inst.entity;

import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "TB_FAVORITE_INST")
public class FavoriteInst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INDIVIDUAL_USER_ID")
    private Individual individualUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTITUTION_USER_ID")
    private Institution institutionUser;

    private LocalDateTime createdAt;
}
