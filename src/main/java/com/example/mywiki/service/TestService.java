package com.example.mywiki.service;

import com.example.mywiki.domain.Test;
import com.example.mywiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {

    @Resource
    private TestMapper testMapper;

    public List<Test> list() {

        return testMapper.list();
    }
}
