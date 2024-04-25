package com.hws.manager.controller.v1;

import com.github.pagehelper.PageInfo;
import com.hws.manager.service.SysUserService;
import com.hws.model.dto.system.AssginRoleDto;
import com.hws.model.dto.system.SysUserDto;
import com.hws.model.entity.system.SysRole;
import com.hws.model.entity.system.SysUser;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "用户条件分页查询接口")
    @PostMapping(value = "/findByPage/{pageNum}/{pageSize}")
    public Result<PageInfo<SysRole>> findByPage(@PathVariable(value = "pageNum") Integer pageNum ,
                                                @PathVariable(value = "pageSize") Integer pageSize,
                                                @RequestBody SysUserDto sysUserDto) {
        return sysUserService.findByPage(sysUserDto , pageNum , pageSize) ;
    }

    @PostMapping(value = "/saveSysUser")
    public Result saveSysUser(@RequestBody SysUser sysUser){
        return sysUserService.saveSysUser(sysUser);
    }
    @PutMapping(value = "/updateSysUser")
    public Result updateSysUser(@RequestBody SysUser sysUser){
        return sysUserService.updataSysUser(sysUser);
    }
    @DeleteMapping(value = "/deleteById/{userId}")
    public Result deleteById(@PathVariable(value = "userId") Long userId) {
        return sysUserService.deleteById(userId) ;
    }

    //分配角色，保存分配数据
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleDto assginRoleDto){
        return sysUserService.doAssign(assginRoleDto);
    }
}
