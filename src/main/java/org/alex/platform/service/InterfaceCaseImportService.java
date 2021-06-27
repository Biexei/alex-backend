package org.alex.platform.service;

import com.alibaba.fastjson.JSONArray;
import org.alex.platform.enums.CaseRule;
import org.alex.platform.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface InterfaceCaseImportService {
    HashMap<String, Integer> importCase(MultipartFile file, Integer projectId, Integer moduleId,Integer requestImportType, Integer suiteId, HttpServletRequest request) throws BusinessException;
    JSONArray generatorInterfaceCase(MultipartFile file, CaseRule caseRule, HttpServletResponse response, boolean isReturnMix, Integer dataType) throws Exception;
}
