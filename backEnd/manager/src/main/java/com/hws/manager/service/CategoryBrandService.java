package com.hws.manager.service;

import com.github.pagehelper.PageInfo;
import com.hws.model.dto.product.CategoryBrandDto;
import com.hws.model.entity.product.CategoryBrand;
import com.hws.model.vo.common.Result;

public interface CategoryBrandService {
    Result<PageInfo<CategoryBrand>> findByPage(Integer page, Integer limit, CategoryBrandDto categoryBrandDto);

    Result save(CategoryBrand categoryBrand);

    Result updateById(CategoryBrand categoryBrand);

    Result deleteById(Long id);

    Result findBrandByCategoryId(Long categoryId);
}
