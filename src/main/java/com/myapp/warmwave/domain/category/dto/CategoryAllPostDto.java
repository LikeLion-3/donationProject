package com.myapp.warmwave.domain.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CategoryAllPostDto {
    @NotNull
    List<String> names;

}
