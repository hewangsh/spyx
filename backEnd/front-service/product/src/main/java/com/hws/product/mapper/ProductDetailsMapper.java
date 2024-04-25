package com.hws.product.mapper;

import com.hws.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDetailsMapper {
    ProductDetails getProductId(Long productId);
}
