package com.hws.manager.service;

import com.hws.model.entity.product.ProductSpec;
import com.hws.model.vo.common.Result;

import java.util.List;

public interface ProductSpecService {
    Result findByPage(Integer page, Integer limit);

    Result save(ProductSpec productSpec);

    Result updateById(ProductSpec productSpec);

    Result removeById(Long id);

    List<ProductSpec> findAll();
}
