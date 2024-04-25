package com.hws.manager.service;

import com.github.pagehelper.PageInfo;
import com.hws.model.entity.product.Brand;
import com.hws.model.vo.common.Result;

public interface BrandService {
    Result<PageInfo<Brand>> findByPage(Integer page, Integer limit);

    Result save(Brand brand);

    Result findAll();

    Result updateById(Brand brand);

    Result deleteById(Long id);
}
