package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface InterfaceCaseImportService {
    @Transactional(rollbackFor = Exception.class)
    HashMap<String, Integer> importCase(MultipartFile file, Integer requestImportType, HttpServletRequest request) throws BusinessException;
}
