package org.alex.platform.service;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImportCaseService {
    @Transactional(rollbackFor = Exception.class)
    Integer insertCaseByJsonYaml(JSONObject jsonObject, String creator, byte source, String importNum) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings({"rawtypes"})
    Integer insertCaseByOffice(List row, String creator, byte source, String importNum) throws BusinessException;
}
