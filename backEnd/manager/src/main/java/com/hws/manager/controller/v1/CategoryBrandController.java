package com.hws.manager.controller.v1;

import com.github.pagehelper.PageInfo;
import com.hws.manager.service.CategoryBrandService;
import com.hws.model.dto.product.CategoryBrandDto;
import com.hws.model.entity.product.CategoryBrand;
import com.hws.model.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/product/categoryBrand")
public class CategoryBrandController {

    @Autowired
    private CategoryBrandService categoryBrandService ;

    //分类品牌的分页查询
    @GetMapping("/{page}/{limit}")
    public Result<PageInfo<CategoryBrand>> findByPage(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            CategoryBrandDto CategoryBrandDto) {
        return categoryBrandService.findByPage(page, limit, CategoryBrandDto);
    }
    //添加
    @PostMapping("/save")
    public Result save(@RequestBody CategoryBrand categoryBrand) {
        return categoryBrandService.save(categoryBrand);
    }
    @PutMapping("updateById")
    public Result updateById(@RequestBody CategoryBrand categoryBrand){
        return categoryBrandService.updateById(categoryBrand);
    }
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id){
        return categoryBrandService.deleteById(id);
    }
    //////////////////////TODO 商品管理//////////////////////////////////////
    @GetMapping("/findBrandByCategoryId/{categoryId}")
    public Result findBrandByCategoryId(@PathVariable Long categoryId){
        return categoryBrandService.findBrandByCategoryId(categoryId);
    }
}