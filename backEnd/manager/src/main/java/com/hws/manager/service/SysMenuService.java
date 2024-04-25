package com.hws.manager.service;

import com.hws.model.entity.system.SysMenu;
import com.hws.model.vo.common.Result;

public interface SysMenuService {
    Result findNodes();

    Result save(SysMenu sysMenu);

    Result updateById(SysMenu sysMenu);

    Result removeById(Long id);

    Result findMenusByUserId();
}
