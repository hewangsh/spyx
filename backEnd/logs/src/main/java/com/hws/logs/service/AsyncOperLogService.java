package com.hws.logs.service;

import com.hws.model.entity.system.SysOperLog;

public interface AsyncOperLogService {			// 保存日志数据
    public abstract void saveSysOperLog(SysOperLog sysOperLog) ;
}