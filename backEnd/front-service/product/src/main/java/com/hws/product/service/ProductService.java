package com.hws.product.service;

import com.github.pagehelper.PageInfo;
import com.hws.model.dto.h5.ProductSkuDto;
import com.hws.model.entity.product.ProductSku;
import com.hws.model.vo.h5.ProductItemVo;

import java.util.List;

public interface ProductService {
    List<ProductSku> selectProductSkuBySal();

    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);

    ProductItemVo item(Long skuId);

    ProductSku getBySkuId(Long skuId);
}
