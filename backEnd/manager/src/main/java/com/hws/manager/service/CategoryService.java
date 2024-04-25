package com.hws.manager.service;

import com.hws.model.vo.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CategoryService {
    Result findCategoryList(int id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file) throws IOException;
}
