package com.hws.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hws.model.dto.h5.ProductSkuDto;
import com.hws.model.entity.product.Product;
import com.hws.model.entity.product.ProductDetails;
import com.hws.model.vo.h5.ProductItemVo;
import com.hws.product.mapper.ProductDetailsMapper;
import com.hws.product.mapper.ProductMapper;
import com.hws.product.mapper.ProductSkuMapper;
import com.hws.model.entity.product.ProductSku;
import com.hws.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Override
    public List<ProductSku> selectProductSkuBySal() {
        return productSkuMapper.selectProductSkuBySal();
    }

    @Override
    public PageInfo<ProductSku> findByPage(Integer page,
                                           Integer limit,
                                           ProductSkuDto productSkuDto) {
        PageHelper.startPage(page, limit);
        List<ProductSku> productSkuList = productSkuMapper.findByPage(productSkuDto);
        return new PageInfo<>(productSkuList);
    }

    @Override
    public ProductItemVo item(Long skuId) {
        //创建一个vo对象，封装数据
        ProductItemVo productItemVo = new ProductItemVo();
        //根据skuid获取sku信息
        ProductSku productSku = productSkuMapper.getById(skuId);
        //根据sku获取producti、商品信息
        Long productId = productSku.getProductId();
        Product product =productMapper.getProductId(productId);
        //根据productid获得商品详情信息
        ProductDetails productDetails=productDetailsMapper.getProductId(productId);
        //map集合：规格对应skuid信息
        HashMap<String, Object> skuSpecHashMap = new HashMap<>();
        //根据商品id获取商品所有sku列表
        List<ProductSku> productSkuList=productSkuMapper.findByProductId(productId);
        productSkuList.forEach(item -> {
            skuSpecHashMap.put(item.getSkuSpec(), item.getId());
        });
        //封装返回
        productItemVo.setProductSku(productSku);
        productItemVo.setProduct(product);
        productItemVo.setDetailsImageUrlList(Arrays.asList(productDetails.getImageUrls().split(",")));
        productItemVo.setSliderUrlList(Arrays.asList(product.getSliderUrls().split(",")));
        productItemVo.setSpecValueList(JSON.parseArray(product.getSpecValue()));
        productItemVo.setSkuSpecValueMap(skuSpecHashMap);
        return productItemVo;
    }

    @Override
    public ProductSku getBySkuId(Long skuId) {
        ProductSku productSku = productSkuMapper.getById(skuId);
        return productSku;
    }
}
