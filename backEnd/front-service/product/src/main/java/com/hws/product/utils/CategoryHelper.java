package com.hws.product.utils;

import com.hws.model.entity.product.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO 封装树形菜单的工具
public class CategoryHelper {
    //TODO 递归实现
    /*
    递归要有一个入口：顶层数据  parentid=0
    查询要有条件：找id
     */
    //要设置static，否则其他无法调用
    public static List<Category> buildTree(List<Category> menuList){
        List<Category> trees = new ArrayList<>();
        for(Category menu : menuList){
            //找到递归操作的入口
            if(menu.getParentId() == 0){
                //递归找下层菜单
                trees.add(findChildren(menu,menuList));
            }
        }
        return trees;
    }

    private static Category findChildren(Category menu, List<Category> menuList) {
        menu.setChildren(new ArrayList<>());
        for(Category child : menuList){
            //该节点的父节点id=menu的id
            if(Objects.equals(child.getParentId(), menu.getId())){
                menu.getChildren().add(findChildren(child,menuList));
            }
        }
        return menu;
    }
}