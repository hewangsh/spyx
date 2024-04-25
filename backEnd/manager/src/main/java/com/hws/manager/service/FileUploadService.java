package com.hws.manager.service;

import com.hws.model.vo.common.Result;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    Result<String> fileUpload(MultipartFile file);
}
