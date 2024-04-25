package com.hws.manager.service;

import com.hws.model.dto.system.SysRoleDto;
import com.hws.model.entity.system.SysRole;
import com.hws.model.vo.common.Result;

import java.util.Map;

public interface SysRoleService {
    Result findByPage(SysRoleDto sysRoleDto, int current, int limit);

    Result saveSysRole(SysRole sysRole);

    Result updataSysRole(SysRole sysRole);

    Result deleteById(Long roleId);

    Result<Map<String, Object>> findAllRoles(Long userId);
}
