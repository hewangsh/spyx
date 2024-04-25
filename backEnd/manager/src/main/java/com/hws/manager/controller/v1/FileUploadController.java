package com.hws.manager.controller.v1;

import com.hws.manager.service.FileUploadService;
import com.hws.model.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/system")
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(value = "/fileUpload")
    //MultipartFile得到上传的文件，TODO 参数名只能叫file
    public Result<String> fileUpload(MultipartFile file){
        return fileUploadService.fileUpload(file);
    }
}
