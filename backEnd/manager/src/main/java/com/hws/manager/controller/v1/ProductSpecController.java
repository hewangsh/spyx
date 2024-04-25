package com.hws.manager.controller.v1;

import com.hws.manager.service.ProductSpecService;
import com.hws.model.entity.product.ProductSpec;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO 商品规格管理
@RestController
@RequestMapping(value="/admin/product/productSpec")
public class ProductSpecController {
    @Autowired
    private ProductSpecService productSpecService;

    @GetMapping("/{page}/{limit}")
    public Result findByPage(@PathVariable Integer page, @PathVariable Integer limit){
        return productSpecService.findByPage(page,limit);
    }
    @PostMapping("save")
    public Result save(@RequestBody ProductSpec productSpec){
        return productSpecService.save(productSpec);
    }
    @PutMapping("updateById")
    public Result updateById(@RequestBody ProductSpec productSpec){
        return productSpecService.updateById(productSpec);
    }
    @DeleteMapping("/deleteById/{id}")
    public Result removeById(@PathVariable Long id){
        return productSpecService.removeById(id);
    }

    @GetMapping("findAll")
    public Result findAll() {
        List<ProductSpec> list = productSpecService.findAll();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }
}
