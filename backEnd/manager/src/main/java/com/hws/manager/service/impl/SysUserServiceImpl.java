package com.hws.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hws.manager.mapper.SysRoleUserMapper;
import com.hws.manager.mapper.SysUserMapper;
import com.hws.manager.properties.UserProperties;
import com.hws.manager.service.SysUserService;
import com.hws.model.dto.system.AssginRoleDto;
import com.hws.model.dto.system.LoginDto;
import com.hws.model.dto.system.SysUserDto;
import com.hws.model.entity.system.SysUser;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.system.LoginVo;
import com.hws.service.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

///////////////////////////////TODO 用户登录////////////////////////////////////////////////
    @Override
    public Result login(LoginDto loginDto) {

        //获取用户输入的验证码和redis对应的key
        String captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();
        codeKey ="user:validate"+codeKey;
        System.out.println(codeKey);
        //根据codekey取redis的验证码
        String redisCode = redisTemplate.opsForValue().get(codeKey);
        //为空或不一致
        if(redisCode == null || !redisCode.equalsIgnoreCase(captcha)) {
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //一致，删除验证码，比较用户名密码。
        redisTemplate.delete(codeKey);
        //得到logindto的用户名
        String userName = loginDto.getUserName();
        //根据用户名查看sys_user表
        SysUser sysUser=sysUserMapper.selectUserInfoByUserName(userName);
        //查不到：登陆失败
        if(sysUser==null){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
        //查到-用户存在,获取输入密码、数据库密码
        String databasePassword = sysUser.getPassword();
        String inputPassword = loginDto.getPassword();
        //因为数据库密码有加密，所以输入密码也要加密后才比较：md5
        inputPassword=DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        //比较输入密码与数据库密码
        //不一致-登陆失败
        if(!databasePassword.equals(inputPassword)){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
        //一致-登陆成功
        //登陆成功指后-生成用户唯一标识：token   用uuid
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //把登录成功的用户信息存入redis:需要先把sysuser转为字符串--用Json.tojsonstring
        redisTemplate.opsForValue().
                set("user:login"+token,
                        JSON.toJSONString(sysUser),
                        7,
                        TimeUnit.DAYS);
        //返回result.build
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

    @Override
    //获取用户登录信息
    public Result getUserInfo(String token) {
        //通过token从redis得到登录信息
        String userJson = redisTemplate.opsForValue()
                .get("user:login" + token);
        //注意登录信息是字符串，需要转为json
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return Result.build(sysUser, ResultCodeEnum.SUCCESS);
    }

    //退出
    @Override
    public Result logout(String token) {
        //删除redis的信息即可
        redisTemplate.delete("user:login"+token);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
/////////////////////////////////TODO 用户管理///////////////////////////////////////////////////
    @Override
    public Result findByPage(SysUserDto sysUserDto, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<SysUser> sysUserList = sysUserMapper.findByPage(sysUserDto) ;
        PageInfo pageInfo = new PageInfo(sysUserList) ;
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result saveSysUser(SysUser sysUser) {
        //先看用户是否重复
        SysUser sysUser1 = sysUserMapper.selectUserInfoByUserName(sysUser.getUserName());
        if(sysUser1!=null){
            throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        //密码加密
        String md5Pwd = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(md5Pwd);
        //TODO 这一步别忘了，设置状态
        sysUser.setStatus(1);
        sysUserMapper.saveSysUser(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updataSysUser(SysUser sysUser) {
        //先看用户是否重复,暂时没实现
        //TODO 这里没有实现修改密码
        sysUserMapper.updataSysUser(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteById(Long userId) {
        sysUserMapper.deleteById(userId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //分配角色
    @Override
    public Result doAssign(AssginRoleDto assginRoleDto) {
        // 删除之前的所有的用户所对应的角色数据
        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId()) ;
        //重新分配
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        //遍历每个要分配的角色id
        for(Long roleId : roleIdList){
            sysRoleUserMapper.doAssign(assginRoleDto.getUserId(),roleId);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
