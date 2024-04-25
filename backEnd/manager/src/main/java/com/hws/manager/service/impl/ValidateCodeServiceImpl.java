package com.hws.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.hws.manager.properties.UserProperties;
import com.hws.manager.service.ValidateCodeService;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    //TODO 一定要加<String,String> 作为一个字典，必须指名键值的类型，否则无法存入redis
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Result<ValidateCodeVo> generateValidateCode() {
        //用hutool工具生成图片验证码:宽，高，位数，干扰线数
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 10);
        String code = circleCaptcha.getCode();   //验证码
        String imageBase64 = circleCaptcha.getImageBase64();   //图片验证码的64位编码
        //将验证码存入redis，key用uuid生成,"user:validate"，value存验证码,并设置时间
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(
                "user:validate"+key, code, 4, TimeUnit.MINUTES);
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);
        //放回的是图像code不是code
        validateCodeVo.setCodeValue("data:image/jpeg;base64," + imageBase64);
        return Result.build(validateCodeVo, ResultCodeEnum.SUCCESS);
    }
}
