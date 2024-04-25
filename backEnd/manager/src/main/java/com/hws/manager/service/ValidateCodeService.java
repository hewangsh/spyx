package com.hws.manager.service;

import com.hws.model.vo.common.Result;
import com.hws.model.vo.system.ValidateCodeVo;

public interface ValidateCodeService {
    public Result<ValidateCodeVo> generateValidateCode();
}
