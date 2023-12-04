package com.myapp.warmwave.common.main.repo;

import com.myapp.warmwave.common.main.dto.MainDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MainRepositoryImpl implements MainRepository {
    private final SqlSessionTemplate sm;

    @Override
    public MainDto getInfo() {
        return sm.selectOne("Main_getCount");
    }
}
