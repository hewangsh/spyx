package com.hws.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.hws.manager.lisentener.ExcelListener;
import com.hws.manager.mapper.CategoryMapper;
import com.hws.manager.service.CategoryService;
import com.hws.model.entity.product.Category;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.product.CategoryExcelVo;
import com.hws.service.exception.MyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result findCategoryList(int id) {
        //根据id查询，返回list
        List<Category> categoryList = categoryMapper.selectByParentId(id);
        //判断每个数据是否有下一层，hasChildren的值，但是数据库中没有这个字段,需要自己设置
        if(!categoryList.isEmpty()){
            for(Category category : categoryList){
                int count = categoryMapper.selectCountByParentId(category.getId());
                if(count > 0){
                    category.setHasChildren(Boolean.TRUE);
                }
                else{
                    category.setHasChildren(Boolean.FALSE);
                }
            }
        }
        return Result.build(categoryList, ResultCodeEnum.SUCCESS);
    }

    //导出数据到xcel
    @Override
    public void exportData(HttpServletResponse response) {
        try{
            //设置响应头信息和其他信息
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类数据", "UTF-8");

            //设置响应头信息,不设置文件无法下载
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".xlsx");
            //调用mapper方法查询所有分类，返回list集合
            List<Category> categoryList= categoryMapper.findAll();

            //把List<Category> 转为CategoryExcelVo.class
            List<CategoryExcelVo> categoryExcelVos=new ArrayList<>();
            for(Category category : categoryList){
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                //把category里面的相同属性复制到categoryExcelVo
                BeanUtils.copyProperties(category,categoryExcelVo);
                categoryExcelVos.add(categoryExcelVo);
            }
            //调用easyexcel的write方法完成写入操作,以数据类的形式
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据").doWrite(categoryExcelVos);
        }
        catch (Exception e) {
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public void importData(MultipartFile file) {
        try{
            //监听器:TODO 用构造器的方式传递mapper
            ExcelListener<CategoryExcelVo> excelListener = new ExcelListener(categoryMapper);
            //读取数据
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class,
                            excelListener).sheet().doRead();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
