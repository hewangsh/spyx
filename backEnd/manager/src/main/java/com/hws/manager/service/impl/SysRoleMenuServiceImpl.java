package com.hws.manager.service.impl;

import com.hws.manager.mapper.SysMenuMapper;
import com.hws.manager.mapper.SysRoleMenuMapper;
import com.hws.manager.service.SysRoleMenuService;
import com.hws.manager.utils.MenuHelper;
import com.hws.model.dto.system.AssginMenuDto;
import com.hws.model.entity.system.SysMenu;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public Result findSysRoleMenuByRoleId(Long roleId) {
        Map<String,Object> map = new HashMap<String,Object>();
        //查询所有菜单
        List<SysMenu> allMenu = sysMenuMapper.findAll();
        //调用工具类方法，封装成要求的格式
        List<SysMenu> treeMenuList= MenuHelper.buildTree(allMenu);
        //查询角色分配过的菜单id列表
        List<Long> menuIds=sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId);
        map.put("roleMenuIds",menuIds);
        map.put("sysMenuList",treeMenuList);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }

    //为角色分配菜单
    @Override
    public Result doAssign(AssginMenuDto assginMenuDto) {
        // 根据角色的id删除其所对应的菜单数据
        sysRoleMenuMapper.deleteByRoleId(assginMenuDto.getRoleId());
        // 获取菜单的id,保存分配数据
        List<Map<String, Number>> menuInfo = assginMenuDto.getMenuIdList();

        if(menuInfo != null && !menuInfo.isEmpty()) {
            sysRoleMenuMapper.doAssign(assginMenuDto) ;
        }
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
}
