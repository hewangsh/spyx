package com.hws.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.hws.model.dto.h5.UserLoginDto;
import com.hws.model.dto.h5.UserRegisterDto;
import com.hws.model.entity.user.UserInfo;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.h5.UserInfoVo;
import com.hws.service.exception.MyException;
import com.hws.user.mapper.UserInfoMapper;
import com.hws.user.service.UserInfoService;
import com.hws.utils.AuthContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private RedisTemplate<String , String> redisTemplate;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void register(UserRegisterDto userRegisterDto) {

		// 获取数据
		String username = userRegisterDto.getUsername();
		String password = userRegisterDto.getPassword();
		String nickName = userRegisterDto.getNickName();
		String code = userRegisterDto.getCode();

		//校验参数
		if(StringUtils.isEmpty(username) ||
				StringUtils.isEmpty(password) ||
				StringUtils.isEmpty(nickName) ||
				StringUtils.isEmpty(code)) {
			throw new MyException(ResultCodeEnum.DATA_ERROR);
		}

		//校验校验验证码
		String codeValueRedis = redisTemplate.opsForValue().get(username);
		if(!code.equals(codeValueRedis)) {
			throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
		}

		UserInfo userInfo = userInfoMapper.getByUsername(username);
		if(null != userInfo) {
			throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
		}

		//保存用户信息
		userInfo = new UserInfo();
		userInfo.setUsername(username);
		userInfo.setNickName(nickName);
		userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
		userInfo.setPhone(username);
		userInfo.setStatus(1);
		userInfo.setSex(0);
		userInfo.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
		userInfoMapper.save(userInfo);
		// 删除Redis中的数据
		redisTemplate.delete(username) ;
	}

	@Override
	public Object login(UserLoginDto userLoginDto) {
		//得到账号密码
		String password = userLoginDto.getPassword();
		String username = userLoginDto.getUsername();
		//根据账号查密码，比较密码是否一致
		UserInfo userInfo = userInfoMapper.getByUsername(username);
		//校验参数
		if(StringUtils.isEmpty(username) ||
				StringUtils.isEmpty(password)) {
			throw new MyException(ResultCodeEnum.DATA_ERROR);
		}
		if(null == userInfo) {
			throw new MyException(ResultCodeEnum.LOGIN_ERROR);
		}
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(userInfo.getPassword())) {
			throw new MyException(ResultCodeEnum.LOGIN_ERROR);
		}
		//校验用户是否被禁用
		if(!userInfo.getStatus().equals(1)) {
			throw new MyException(ResultCodeEnum.ACCOUNT_STOP);
		}
		//生成token
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println("登录生成的token:" + token);
		//token存在redis
		redisTemplate.opsForValue().set("user:spzx"+token,
				JSON.toJSONString(userInfo),30, TimeUnit.DAYS);
		//返回token
		return token;
	}

	@Override
	public UserInfoVo getCurrentUserInfo(String token) {
//		String userInfo = redisTemplate.opsForValue().get("user:spzx" + token);
//		if(StringUtils.isEmpty(userInfo)) {
//			throw new MyException(ResultCodeEnum.DATA_ERROR);
//		}
//		UserInfo userInfoJson = JSON.parseObject(userInfo, UserInfo.class);
		UserInfo userInfo = AuthContextUtil.getUserInfo();
		UserInfoVo userInfoVo = new UserInfoVo();
		BeanUtils.copyProperties(userInfo, userInfoVo);
		return userInfoVo;
	}
}