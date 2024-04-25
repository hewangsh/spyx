package com.hws.product.controller.v1;

import com.github.pagehelper.PageInfo;
import com.hws.model.dto.h5.ProductSkuDto;
import com.hws.model.entity.product.ProductSku;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.h5.ProductItemVo;
import com.hws.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "商品列表管理")
@RestController
@RequestMapping(value="/api/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@Operation(summary = "分页查询")
	@GetMapping(value = "/{page}/{limit}")
	public Result<PageInfo<ProductSku>> findByPage(@Parameter(name = "page", description = "当前页码", required = true) @PathVariable Integer page,
												   @Parameter(name = "limit", description = "每页记录数", required = true) @PathVariable Integer limit,
												   @Parameter(name = "productSkuDto", description = "搜索条件对象", required = false) ProductSkuDto productSkuDto) {
		PageInfo<ProductSku> pageInfo = productService.findByPage(page, limit, productSkuDto);
		return Result.build(pageInfo , ResultCodeEnum.SUCCESS) ;
	}

	@Operation(summary = "商品详情")
	@GetMapping("item/{skuId}")
	public Result<ProductItemVo> item(@Parameter(name = "skuId", description = "商品skuId", required = true)
										  @PathVariable Long skuId) {
		ProductItemVo productItemVo = productService.item(skuId);
		return Result.build(productItemVo , ResultCodeEnum.SUCCESS);
	}
	@Operation(summary = "获取商品sku信息")
	//不是为了返回给前端，所以不用result，是远程调用给cart
	@GetMapping("getBySkuId/{skuId}")
	public ProductSku getBySkuId(@Parameter(name = "skuId", description = "商品skuId", required = true) @PathVariable Long skuId) {
		ProductSku productSku = productService.getBySkuId(skuId);
		return productSku;
	}

}