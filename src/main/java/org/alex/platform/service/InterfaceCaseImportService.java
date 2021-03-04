package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface InterfaceCaseImportService {
    HashMap<String, Integer> importCase(MultipartFile file, Integer requestImportType, Integer suiteId, HttpServletRequest request) throws BusinessException;
}
