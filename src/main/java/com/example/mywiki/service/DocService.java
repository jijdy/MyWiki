package com.example.mywiki.service;

import com.example.mywiki.domain.Doc;
import com.example.mywiki.domain.DocExample;
import com.example.mywiki.mapper.DocMapper;
import com.example.mywiki.req.DocQueryReq;
import com.example.mywiki.req.DocSaveReq;
import com.example.mywiki.resp.DocQueryResp;
import com.example.mywiki.resp.PageResp;
import com.example.mywiki.util.CopyUtil;
import com.example.mywiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    private static final Logger Log = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<DocQueryResp> list(DocQueryReq req) {

        DocExample docExample = new DocExample();
        if (!ObjectUtils.isEmpty(req.getName())) {
            DocExample.Criteria criteria = docExample.createCriteria();
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        Log.info("总行数：{}",pageInfo.getTotal());
        Log.info("总页数：{}",pageInfo.getPages());


        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public List<DocQueryResp> all(DocQueryReq req) {

        DocExample docExample = new DocExample();
        if (!ObjectUtils.isEmpty(req.getName())) {
            DocExample.Criteria criteria = docExample.createCriteria();
            criteria.andNameLike("%" + req.getName() + "%");
        }
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }

    public void save(DocSaveReq req) {
        Log.info(req.toString());
        Doc doc = CopyUtil.copy(req,Doc.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            //若id为空，则数据库中无数据，则插入数据
            //使用了雪花算法，根据时间戳来对更新的id进行计算得到一个long类型的数据
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        } else {
            //否则更新数据即可
            docMapper.updateByPrimaryKey(doc);
        }
    }

    public void delete(long id) {
        docMapper.deleteByPrimaryKey(id);
    }
}
