package org.alex.platform.service;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImportCaseService {
    @Transactional(rollbackFor = Exception.class)
    Integer insertCaseByJsonYaml(JSONObject jsonObject, Integer projectId, Integer moduleId, String creator, byte source, String importNum) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings({"rawtypes"})
    Integer insertCaseByOffice(List row, Integer projectId, Integer moduleId, String creator, byte source, String importNum) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    Integer insertCaseByHar(JSONObject entry, Integer projectId, Integer moduleId, String creator, String importNum) throws BusinessException;
}
