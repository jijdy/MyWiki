package com.example.mywiki.service;

import com.example.mywiki.domain.Ebook;
import com.example.mywiki.domain.EbookExample;
import com.example.mywiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list(String name) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + name + "%");
        return ebookMapper.selectByExample(ebookExample);
    }
}
