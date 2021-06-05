package com.example.mywiki.service;

import com.example.mywiki.domain.Ebook;
import com.example.mywiki.domain.EbookExample;
import com.example.mywiki.mapper.EbookMapper;
import com.example.mywiki.req.EbookReq;
import com.example.mywiki.resp.EbookResp;
import com.example.mywiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    private static final Logger Log = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {

        EbookExample ebookExample = new EbookExample();
        if (!ObjectUtils.isEmpty(req.getName())) {
            EbookExample.Criteria criteria = ebookExample.createCriteria();
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(1,3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        Log.info("总行数：{}",pageInfo.getTotal());
        Log.info("总页数：{}",pageInfo.getPages());


//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebooks) {
//            EbookResp resp = new EbookResp();
//            BeanUtils.copyProperties(ebook,resp);
//            respList.add(resp);;
//        }

        return CopyUtil.copyList(ebookList, EbookResp.class);
    }
}
