package com.hws.manager.mapper;

import com.hws.model.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {

    List<SysMenu> findAll();

    void save(SysMenu sysMenu);

    void updateById(SysMenu sysMenu);

    int selectCount(Long id);

    void removeById(Long id);

    List<SysMenu> findMenusByUserId(Long userId);

    SysMenu selectParentMenu(Long parentId);
}
