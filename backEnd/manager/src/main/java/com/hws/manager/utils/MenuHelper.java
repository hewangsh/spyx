package com.hws.manager.utils;

import com.hws.model.entity.system.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO 封装树形菜单的工具
public class MenuHelper {
    //TODO 递归实现
    /*
    递归要有一个入口：顶层数据  parentid=0
    查询要有条件：找id
     */
    //要设置static，否则其他无法调用
    public static List<SysMenu> buildTree(List<SysMenu> menuList){
        List<SysMenu> trees = new ArrayList<>();
        for(SysMenu menu : menuList){
            //找到递归操作的入口
            if(menu.getParentId() == 0){
                //递归找下层菜单
                trees.add(findChildren(menu,menuList));
            }
        }
        return trees;
    }

    private static SysMenu findChildren(SysMenu menu, List<SysMenu> menuList) {
        menu.setChildren(new ArrayList<>());
        for(SysMenu child : menuList){
            //该节点的父节点id=menu的id
            if(Objects.equals(child.getParentId(), menu.getId())){
                menu.getChildren().add(findChildren(child,menuList));
            }
        }
        return menu;
    }
}
