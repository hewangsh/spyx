package com.hws.utils;

import com.hws.model.entity.system.SysUser;
import com.hws.model.entity.user.UserInfo;

public class AuthContextUtil {
    private static final ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>() ;


    // 定义存储数据的静态方法
    public static void setUserInfo(UserInfo userInfo) {
        userInfoThreadLocal.set(userInfo);
    }

    // 定义获取数据的方法
    public static UserInfo getUserInfo() {
        return userInfoThreadLocal.get() ;
    }

    // 删除数据的方法
    public static void removeUserInfo() {
        userInfoThreadLocal.remove();
    }
    //TODO 创建一个threadlocal对象
    private static final ThreadLocal<SysUser> AUTH_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static SysUser getAuthContext() {
        return AUTH_CONTEXT_THREAD_LOCAL.get();
    }

    public static void setAuthContext(SysUser sysUser) {
        AUTH_CONTEXT_THREAD_LOCAL.set(sysUser);
    }

    public static void removeAuthContext() {
        AUTH_CONTEXT_THREAD_LOCAL.remove();
    }
}
