package com.hws.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.hws.product.mapper.CategoryMapper;
import com.hws.model.entity.product.Category;
import com.hws.product.service.CategoryService;
import com.hws.product.utils.CategoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String , String> redisTemplate ;
    //TODO 不用缓存添加
    @Override
    public List<Category> selectOneCategory() {
        //查询redis是否有所有的一级分类
        String categoryOne = redisTemplate.opsForValue().get("category1:one");
        //有则直接返回
        if(categoryOne !=null){
            List<Category> categoryList = JSON.parseArray(categoryOne, Category.class);
            System.out.println("从Redis缓存中查询到了所有的一级分类数据");
            return categoryList;
        }
        //无则查数据库
        List<Category> categoryList = categoryMapper.selectOneCategory();
        System.out.println("从数据库中查询到了所有的一级分类数据");
        redisTemplate.opsForValue().set("category:one",
                JSON.toJSONString(categoryList),
                1, TimeUnit.HOURS);
        return categoryList;
    }
    //TODO redis缓存添加
    @Cacheable(value = "category1" ,key = "'all'")   //形成key：category::all
    @Override
    public List<Category> findCategoryTree() {
        //查询所有的分类
        List<Category> categoryList=categoryMapper.findCategoryTree();
        //递归
        List<Category> categoryTree= CategoryHelper.buildTree(categoryList);
        return categoryTree;
    }
}
