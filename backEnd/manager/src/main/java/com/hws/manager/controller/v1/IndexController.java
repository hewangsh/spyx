package com.hws.manager.controller.v1;

import com.hws.manager.service.SysMenuService;
import com.hws.manager.service.SysUserService;
import com.hws.manager.service.ValidateCodeService;
import com.hws.model.dto.system.LoginDto;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.system.LoginVo;
import com.hws.model.vo.system.ValidateCodeVo;
import com.hws.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    ValidateCodeService validateCodeService;
    @Autowired
    private SysMenuService sysMenuService;

    //生成图像验证码
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        return validateCodeService.generateValidateCode();
    }

    @Operation(summary="用户登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto) {
        return sysUserService.login(loginDto);
    }

    @GetMapping(value = "/getUserInfo")
    //通过注解请求头得到token
//    public Result getUserInfo(@RequestHeader(name = "token") String token) {
//        return sysUserService.getUserInfo(token) ;
//    }
    //TODO 优化获取信息的代码，因为有了threadlocal，直接从这里取用户信息即可
    public Result getUserInfo() {
        //AuthContextUtil.getAuthContext()得到的就是sysUser对象
        return Result.build(AuthContextUtil.getAuthContext(), ResultCodeEnum.SUCCESS);
    }

    //退出接口
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(value = "token") String token) {
        sysUserService.logout(token) ;
        return sysUserService.logout(token) ;
    }

    //查询用户可以操作的菜单
    @GetMapping("/menus")
    public Result menus(){
        return sysMenuService.findMenusByUserId();
    }
}
