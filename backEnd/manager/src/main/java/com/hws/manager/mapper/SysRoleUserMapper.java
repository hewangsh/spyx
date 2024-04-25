package com.hws.manager.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//TODO 操作不同的表设置不同的mapper
@Mapper
public interface SysRoleUserMapper {
    void deleteByUserId(Long userId);

    void doAssign(Long userId, Long roleId);

    List<Long> selectRoleIdByUserId(Long userId);
}
