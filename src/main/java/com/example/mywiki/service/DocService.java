package com.example.mywiki.service;

import com.example.mywiki.domain.Content;
import com.example.mywiki.domain.ContentExample;
import com.example.mywiki.domain.Doc;
import com.example.mywiki.domain.DocExample;
import com.example.mywiki.exception.BusinessException;
import com.example.mywiki.exception.BusinessExceptionCode;
import com.example.mywiki.mapper.ContentMapper;
import com.example.mywiki.mapper.DocMapper;
import com.example.mywiki.mapper.DocMapperCust;
import com.example.mywiki.req.DocQueryReq;
import com.example.mywiki.req.DocSaveReq;
import com.example.mywiki.resp.DocQueryResp;
import com.example.mywiki.resp.PageResp;
import com.example.mywiki.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private DocMapperCust docMapperCust;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private ReidsRepeatUtil reidsRepeatUtil;

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

    //查询所有的文档
    public List<DocQueryResp> all(Long ebookId) {

        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        if (!ObjectUtils.isEmpty(ebookId)) {
            DocExample.Criteria criteria = docExample.createCriteria();
            criteria.andNameLike("%" + ebookId + "%");
        }
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        return CopyUtil.copyList(docList, DocQueryResp.class);
    }

    //保存文档信息
    public void save(DocSaveReq req) {
        Log.info(req.toString());
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            doc.setId(snowFlake.nextId());

            /*
            *在初始创建文档表时将其文档阅读数和点赞数置为0，
            * 因为req传入时没有该参数，默认为null，之后对null不能进行操作
             */
            doc.setViewCount(0);
            doc.setVoteCount(0);

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

    //获取该文档的内容
    public String getContent(long id) {
        Content s = contentMapper.selectByPrimaryKey(id);
//        Log.error("文档被访问：{}", id);
        docMapperCust.increaseViewCount(id);//每一次查询文档都将其阅读数 + 1
        if (ObjectUtils.isEmpty(s)) {
            return "";
        }else
        return s.getContent();
    }

    //为点赞进行加一操作
    public void vote(Long id) {
        if (reidsRepeatUtil.RedisRepeat("vote_id" + RequestUtil.getRemoteAddr() + "_" + id, 3600 * 24L)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.USER_VOTE_REPEAT);
        }

    }

    /*
     * 统计文档表数据并更新到电子书中
     * */
    public void updateEbook() {
        docMapperCust.updateEbookInfo();
    }
}