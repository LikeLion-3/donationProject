package com.myapp.warmwave.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@ToString
@DiscriminatorValue("institution")
public class Institution extends User {
    @Column(unique = true)
    private String institutionName;

    @Column(unique = true)
    private String registerNum;

    private Boolean isApprove;

    public void approve() {
        this.isApprove = true;
    }

    @Override
    public String getName() {
        return this.institutionName;
    }
}
