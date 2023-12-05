package com.myapp.warmwave.domain.community.mapper;

import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.entity.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CommunityMapper extends CommunityUpdateMapper{
    @Mapping(target = "title", source = "dto.title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contents", source = "dto.contents", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", source = "dto.category", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Community updateCommunity(@MappingTarget Community community, CommunityPatchDto dto);
}
