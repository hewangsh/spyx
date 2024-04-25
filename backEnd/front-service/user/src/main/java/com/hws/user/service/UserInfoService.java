package com.hws.user.service;

import com.hws.model.dto.h5.UserLoginDto;
import com.hws.model.dto.h5.UserRegisterDto;
import com.hws.model.vo.h5.UserInfoVo;

public interface UserInfoService {
    void register(UserRegisterDto userRegisterDto);

    Object login(UserLoginDto userLoginDto);

    UserInfoVo getCurrentUserInfo(String token);
}
