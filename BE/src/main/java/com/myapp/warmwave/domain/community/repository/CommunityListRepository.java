package com.myapp.warmwave.domain.community.repository;

import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;

public interface CommunityListRepository {
    Page<CommunityListResponseDto> findAllCommunities(@PageableDefault Pageable pageable, String sort);
}
