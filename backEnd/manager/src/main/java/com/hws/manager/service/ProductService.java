package com.hws.manager.service;

import com.hws.model.dto.product.ProductDto;
import com.hws.model.entity.product.Product;
import com.hws.model.vo.common.Result;

public interface ProductService {
    Result findByPage(Integer page, Integer limit, ProductDto productDto);

    void save(Product product);

    Product getById(Long id);

    void updateById(Product product);

    void deleteById(Long id);

    void updateAuditStatus(Long id, Integer auditStatus);

    void updateStatus(Long id, Integer status);
}
