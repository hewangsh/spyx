package com.hws.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hws.manager.mapper.CategoryBrandMapper;
import com.hws.manager.service.CategoryBrandService;
import com.hws.model.dto.product.CategoryBrandDto;
import com.hws.model.entity.product.Brand;
import com.hws.model.entity.product.CategoryBrand;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CategoryBrandServiceImpl implements CategoryBrandService {
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result<PageInfo<CategoryBrand>> findByPage(Integer page, Integer limit, CategoryBrandDto categoryBrandDto) {
        PageHelper.startPage(page, limit);
        List<CategoryBrand> categoryBrandList= categoryBrandMapper.findByPage(categoryBrandDto);
        PageInfo pageInfo = new PageInfo(categoryBrandList);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
    //添加
    @Override
    public Result save(CategoryBrand categoryBrand) {
        categoryBrandMapper.save(categoryBrand);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updateById(CategoryBrand categoryBrand) {
        categoryBrandMapper.updateById(categoryBrand);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteById(Long id) {
        categoryBrandMapper.deleteById(id);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
//////////////////////////////////////////////////////////////////////////////
    @Override
    public Result findBrandByCategoryId(Long categoryId) {
        List<Brand> brandList= categoryBrandMapper.findBrandByCategoryId(categoryId);
        return Result.build(brandList,ResultCodeEnum.SUCCESS);
    }

}
