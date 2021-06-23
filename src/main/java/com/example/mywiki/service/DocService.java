package com.example.mywiki.service;

import com.example.mywiki.domain.Content;
import com.example.mywiki.domain.ContentExample;
import com.example.mywiki.domain.Doc;
import com.example.mywiki.domain.DocExample;
import com.example.mywiki.mapper.ContentMapper;
import com.example.mywiki.mapper.DocMapper;
import com.example.mywiki.req.DocQueryReq;
import com.example.mywiki.req.DocSaveReq;
import com.example.mywiki.resp.DocQueryResp;
import com.example.mywiki.resp.PageResp;
import com.example.mywiki.util.CopyUtil;
import com.example.mywiki.util.SnowFlake;
import com.example.mywiki.util.StringUtil;
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

    @Resource
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<DocQueryResp> list(DocQueryReq req) {

        DocExample docExample = new DocExample();
        if (!ObjectUtils.isEmpty(req.getName())) {
            DocExample.Criteria criteria = docExample.createCriteria();
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        Log.info("总行数：{}", pageInfo.getTotal());
        Log.info("总页数：{}", pageInfo.getPages());


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

        return CopyUtil.copyList(docList, DocQueryResp.class);
    }

    public void save(DocSaveReq req) {
        Log.info(req.toString());
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);

            //此时id已经新增，需要获取到值再进行相应操作
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //否则更新数据即可
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void delete(long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);

        List<Long> lids = StringUtil.StrToLong(ids);
        ContentExample contentExample = new ContentExample();
        ContentExample.Criteria criteria1 = contentExample.createCriteria();
        criteria1.andIdIn(lids);
        contentMapper.deleteByExample(contentExample);
    }

    public String getContent(long id) {
        Content s = contentMapper.selectByPrimaryKey(id);
        if (ObjectUtils.isEmpty(s)) {
            return null;
        }else
        return s.getContent();
    }
}