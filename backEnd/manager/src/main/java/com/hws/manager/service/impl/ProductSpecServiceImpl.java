package com.hws.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hws.manager.mapper.ProductSpecMapper;
import com.hws.manager.service.ProductSpecService;
import com.hws.model.entity.product.ProductSpec;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpecServiceImpl implements ProductSpecService {
    @Autowired
    private ProductSpecMapper productSpecMapper;

    @Override
    public Result findByPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<ProductSpec> productSpecList= productSpecMapper.findByPage();
        PageInfo<ProductSpec> pageInfo = new PageInfo<>(productSpecList);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result save(ProductSpec productSpec) {
        productSpecMapper.save(productSpec);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updateById(ProductSpec productSpec) {
        productSpecMapper.updateById(productSpec);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result removeById(Long id) {
        productSpecMapper.removeById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public List<ProductSpec> findAll() {
        return productSpecMapper.findAll();
    }
}
