package com.hws.manager.controller.v1;

import com.hws.manager.service.SysRoleService;
import com.hws.model.dto.system.SysRoleDto;
import com.hws.model.entity.system.SysRole;
import com.hws.model.vo.common.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "角色管理接口")
@RequestMapping(value = "/admin/system/sysRole")
@RestController

public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    //角色列表的方法
    @PostMapping("/findByPage/{current}/{limit}")
    public Result findByPage(@PathVariable int current,
                             @PathVariable int limit,
                             @RequestBody SysRoleDto sysRoleDto) {
        //pageInfo插件实现分页
        return sysRoleService.findByPage(sysRoleDto,current,limit);
    }

    @PostMapping(value = "/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole){
        return sysRoleService.saveSysRole(sysRole);
    }

    @PutMapping(value = "/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole){
        return sysRoleService.updataSysRole(sysRole);
    }

    @DeleteMapping(value = "/deleteById/{roleId}")
    public Result deleteById(@PathVariable(value = "roleId") Long roleId){
        return sysRoleService.deleteById(roleId);
    }
    //查所有角色
    @GetMapping(value = "/findAllRoles/{userId}")
    public Result<Map<String , Object>> findAllRoles(
            @PathVariable(value = "userId") Long userId){
        return sysRoleService.findAllRoles(userId);
    }

}
