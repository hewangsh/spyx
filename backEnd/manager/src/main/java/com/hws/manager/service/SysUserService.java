package com.hws.manager.service;

import com.github.pagehelper.PageInfo;
import com.hws.model.dto.system.AssginRoleDto;
import com.hws.model.dto.system.LoginDto;
import com.hws.model.dto.system.SysUserDto;
import com.hws.model.entity.system.SysUser;
import com.hws.model.vo.common.Result;

public interface SysUserService {
    public Result login(LoginDto loginDto);

    public Result getUserInfo(String token);

    Result logout(String token);

    Result findByPage(SysUserDto sysUserDto, Integer pageNum, Integer pageSize);


    Result saveSysUser(SysUser sysUser);

    Result updataSysUser(SysUser sysUser);

    Result deleteById(Long userId);

    Result doAssign(AssginRoleDto assginRoleDto);
}
