package com.hws.manager.controller.v1;


import com.hws.manager.service.SysRoleMenuService;
import com.hws.model.dto.system.AssginMenuDto;
import com.hws.model.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/system/sysRoleMenu")
public class SysRoleMenuController {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    //查询所有菜单和查询角色分配过的菜单id
    @GetMapping(value = "/findSysRoleMenuByRoleId/{roleId}")
    public Result findSysRoleMenuByRoleId(@PathVariable(value = "roleId") Long roleId){
        return sysRoleMenuService.findSysRoleMenuByRoleId(roleId);
    }
    //保存角色分配的菜单数据
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuDto assginMenuDto){
        return sysRoleMenuService.doAssign(assginMenuDto);
    }
}
