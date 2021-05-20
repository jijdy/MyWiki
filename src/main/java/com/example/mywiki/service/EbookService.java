package com.example.mywiki.service;

import com.example.mywiki.domain.Ebook;
import com.example.mywiki.domain.EbookExample;
import com.example.mywiki.mapper.EbookMapper;
import com.example.mywiki.req.EbookReq;
import com.example.mywiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> respList = new ArrayList<>();
        for (Ebook ebook : ebooks) {
            EbookResp resp = new EbookResp();
            BeanUtils.copyProperties(ebook,resp);
            respList.add(resp);;
        }
        return respList;
    }
}
