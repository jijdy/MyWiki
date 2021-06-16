package com.example.mywiki.service;

import com.example.mywiki.domain.Category;
import com.example.mywiki.domain.CategoryExample;
import com.example.mywiki.mapper.CategoryMapper;
import com.example.mywiki.req.CategoryQueryReq;
import com.example.mywiki.req.CategorySaveReq;
import com.example.mywiki.resp.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger Log = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {

        CategoryExample categoryExample = new CategoryExample();
        if (!ObjectUtils.isEmpty(req.getName())) {
            CategoryExample.Criteria criteria = categoryExample.createCriteria();
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        Log.info("总行数：{}",pageInfo.getTotal());
        Log.info("总页数：{}",pageInfo.getPages());


        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public List<CategoryQueryResp> all(CategoryQueryReq req) {

        CategoryExample categoryExample = new CategoryExample();
        if (!ObjectUtils.isEmpty(req.getName())) {
            CategoryExample.Criteria criteria = categoryExample.createCriteria();
            criteria.andNameLike("%" + req.getName() + "%");
        }
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return list;
    }

    public void save(CategorySaveReq req) {
        Log.info(req.toString());
        Category category = CopyUtil.copy(req,Category.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            //若id为空，则数据库中无数据，则插入数据
            //使用了雪花算法，根据时间戳来对更新的id进行计算得到一个long类型的数据
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            //否则更新数据即可
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
