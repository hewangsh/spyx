package com.hws.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hws.manager.mapper.BrandMapper;
import com.hws.manager.service.BrandService;
import com.hws.model.entity.product.Brand;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    @Override
    public Result<PageInfo<Brand>> findByPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Brand> brandList = brandMapper.findByPage() ;
        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result save(Brand brand) {
        brandMapper.save(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


    @Override
    public Result findAll() {
        List<Brand> brandList= brandMapper.findByPage();
        return Result.build(brandList, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updateById(Brand brand) {
        brandMapper.updateById(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteById(Long id) {
        brandMapper.deleteById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
