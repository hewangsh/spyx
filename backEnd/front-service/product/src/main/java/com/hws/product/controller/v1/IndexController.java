package com.hws.product.controller.v1;

import com.hws.model.entity.product.Category;
import com.hws.model.entity.product.ProductSku;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.h5.IndexVo;
import com.hws.product.service.CategoryService;
import com.hws.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/product/index")
public class IndexController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "获取首页数据")
    @GetMapping
    public Result<IndexVo> findData(){
        //返回所有的一级分类
        List<Category> categoryList=categoryService.selectOneCategory();
        //根据销量排序，获取前10条记录
        List<ProductSku> productSkuList=productService.selectProductSkuBySal();
        //把数据封装到indexVo
        IndexVo indexVo=new IndexVo();
        indexVo.setCategoryList(categoryList);
        indexVo.setProductSkuList(productSkuList);
        return Result.build(indexVo, ResultCodeEnum.SUCCESS);
    }
}
