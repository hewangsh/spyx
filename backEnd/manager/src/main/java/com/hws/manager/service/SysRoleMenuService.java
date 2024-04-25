package com.hws.manager.service;

import com.hws.model.dto.system.AssginMenuDto;
import com.hws.model.vo.common.Result;

public interface SysRoleMenuService {
    Result findSysRoleMenuByRoleId(Long roleId);

    Result doAssign(AssginMenuDto assginMenuDto);
}
