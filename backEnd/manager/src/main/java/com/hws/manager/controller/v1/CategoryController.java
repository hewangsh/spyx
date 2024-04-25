package com.hws.manager.controller.v1;

import com.hws.manager.service.CategoryService;
import com.hws.model.vo.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value="/admin/product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //分类列表，每次只查一层数据---懒加载
    @GetMapping("/findCategoryList/{id}")
    public Result findCategoryList(@PathVariable int id) {
        return categoryService.findCategoryList(id);
    }

    //导出数据到xcel:导出数据不需要返回数据
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response){
        categoryService.exportData(response);
    }
    //TODO MultipartFile file是用来得到上传的文件的
    @PostMapping("importData")
    public void importData(MultipartFile file) throws IOException {
        categoryService.importData(file);
    }
}
