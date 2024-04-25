package com.hws.manager.controller.v1;

import com.github.pagehelper.PageInfo;
import com.hws.logs.annotation.Log;
import com.hws.logs.enums.OperatorType;
import com.hws.manager.service.BrandService;
import com.hws.model.entity.product.Brand;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// com.atguigu.spzx.manager.controller
@RestController
@RequestMapping(value="/admin/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService ;
    //分页查询
    @Log(title = "品牌列表",businessType = 0,operatorType = OperatorType.MANAGE)
    @GetMapping("/{page}/{limit}")
    public Result<PageInfo<Brand>> findByPage(@PathVariable Integer page, @PathVariable Integer limit) {
        return brandService.findByPage(page, limit);
    }
    //添加
    @PostMapping("/save")
    public Result save(@RequestBody Brand brand){
        return brandService.save(brand);
    }

    @PutMapping("updateById")
    public Result updateById(@RequestBody Brand brand){
        return brandService.updateById(brand);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id){
        return brandService.deleteById(id);
    }

    //查询所有品牌
    @GetMapping("/findAll")
    public Result findAll(){
        return brandService.findAll();
    }
}