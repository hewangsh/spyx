package com.hws.product.mapper;

import com.hws.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    Product getProductId(Long productId);
}
