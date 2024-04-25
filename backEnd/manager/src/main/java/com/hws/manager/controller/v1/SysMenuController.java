package com.hws.manager.controller.v1;

import com.hws.manager.service.SysMenuService;
import com.hws.model.entity.system.SysMenu;
import com.hws.model.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
    @GetMapping("/findNodes")
    public Result findNodes() {
        return sysMenuService.findNodes();
    }

    //菜单添加
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu){
        return sysMenuService.save(sysMenu);
    }

    //菜单修改
    @PutMapping("/updateById")
    public Result updateById(@RequestBody SysMenu sysMenu){
        return sysMenuService.updateById(sysMenu);
    }
    @DeleteMapping("/removeById/{id}")
    public Result removeById(@PathVariable Long id){
        return sysMenuService.removeById(id);
    }
}
