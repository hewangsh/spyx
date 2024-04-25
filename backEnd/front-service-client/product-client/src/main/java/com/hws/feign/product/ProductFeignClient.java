package com.hws.feign.product;

import com.hws.model.entity.product.ProductSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//指定远程的服务名称
@FeignClient(value = "service-product")
public interface ProductFeignClient {

    //路径写完整
    @GetMapping("/api/product/getBySkuId/{skuId}")
    ProductSku getBySkuId(@PathVariable("skuId") Long skuId) ;
}