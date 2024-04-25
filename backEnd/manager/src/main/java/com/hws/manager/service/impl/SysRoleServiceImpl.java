package com.hws.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hws.manager.mapper.SysRoleMapper;
import com.hws.manager.mapper.SysRoleUserMapper;
import com.hws.manager.service.SysRoleService;
import com.hws.model.dto.system.SysRoleDto;
import com.hws.model.entity.system.SysRole;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public Result findByPage(SysRoleDto sysRoleDto, int current, int limit) {
        //设置分页的参数，原理：先查询所有数据，再根据PageHelper取current页的limit条数据
        PageHelper.startPage(current, limit);
        //TODO 根据条件查询所有数据:list类型
        List<SysRole> list=sysRoleMapper.findByPage(sysRoleDto);
        //pageInfo插件实现分页，分页查询得到的是一个PageInfo<SysRole>对象
        PageInfo<SysRole> pageInfo=new PageInfo<>(list);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result saveSysRole(SysRole sysRole) {
        sysRoleMapper.saveSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updataSysRole(SysRole sysRole) {
        sysRoleMapper.updataSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteById(Long roleId) {
        sysRoleMapper.deleteById(roleId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<Map<String, Object>> findAllRoles(Long userId) {
        //TODO 用map是为了返回两部分数据，一部分是所有角色，一部分是用户已分配的角色，方便回显
        //查询所有角色
        List<SysRole> list=sysRoleMapper.findAllRoles();
        Map<String, Object> map=new HashMap<>();
        map.put("allRolesList", list);
        //查询用户分配过的角色
        List<Long> roleIdList=sysRoleUserMapper.selectRoleIdByUserId(userId);
        map.put("sysUserRoles", roleIdList);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }

}
