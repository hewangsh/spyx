package com.hws.product.service;

import com.hws.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    List<Category> selectOneCategory();

    List<Category> findCategoryTree();
}
