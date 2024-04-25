package com.hws.manager.mapper;

import com.hws.model.dto.system.SysUserDto;
import com.hws.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {
    public SysUser selectUserInfoByUserName(String userName);

    List<SysUser> findByPage(SysUserDto sysUserDto);

    void saveSysUser(SysUser sysUser);

    void updataSysUser(SysUser sysUser);

    void deleteById(Long userId);
}
