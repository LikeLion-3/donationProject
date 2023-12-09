package com.myapp.warmwave.domain.address.entity;

import com.myapp.warmwave.common.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "TB_ADDRESS")
@EqualsAndHashCode
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullAddr;

    // 시,도
    private String sdName;

    // 시,군,구
    private String sggName;

    // 상세주소
    private String details;

    @Enumerated(EnumType.STRING)
    private Role userType;

    public void update(String fullAddr, String sdName, String sggName, String details) {
        this.fullAddr = fullAddr;
        this.sdName = sdName;
        this.sggName = sggName;
        this.details = details;
    }
}
