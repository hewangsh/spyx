package com.hws.manager.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hws.manager.properties.UserProperties;
import com.hws.model.entity.system.SysUser;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserProperties userProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        //获取请求方式
        //如果请求是option（预检请求）直接放行
        String method = request.getMethod();
        if("OPTIONS".equals(method)){
            return true;
        }
        //反之，从请求头获取token
        String token = request.getHeader("token");
        //如果token是空，返回错误信息
        if(token == null){
            responseNoLoginInfo(response);
            return false;
        }
        //反之，拿着token去查询redis
        String userInfoString = redisTemplate.opsForValue().get("user:login" + token);
        System.out.println("拦截器用户信息："+userInfoString);
        //查不到，返回错误信息
        if(userInfoString == null){
            responseNoLoginInfo(response);
            return false;
        }
        //查得到，把用户信息放到threadlocal里,TODO 记得在后面填类SysUser.class
        //TODO 要先把用户信息转为json对象
        SysUser sysUser = JSON.parseObject(userInfoString, SysUser.class);
        AuthContextUtil.setAuthContext(sysUser);
        //把redis用户信息更新过期时间
        redisTemplate.expire("user:login"+token,
                30, TimeUnit.MINUTES);
        //放行
        return true;
    }

    //响应208状态码给前端
    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        //删除threadlocal
        AuthContextUtil.removeAuthContext();
    }
}
