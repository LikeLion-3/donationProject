package com.myapp.warmwave.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myapp.warmwave.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@SuperBuilder
@ToString
@DiscriminatorValue("institution")
public class Institution extends User {
    @Column(unique = true)
    private String institutionName;

    @Column(unique = true)
    private String registerNum;

    private Boolean isApprove;

    @OneToOne(mappedBy = "institution",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private Image institutionImage;    // 사업자등록증 image

    public void approve() {
        this.isApprove = true;
    }

    @Override
    public String getName() {
        return this.institutionName;
    }

    public void addInstitutionImage(Image institutionImage) {
        this.institutionImage = institutionImage;
    }
}