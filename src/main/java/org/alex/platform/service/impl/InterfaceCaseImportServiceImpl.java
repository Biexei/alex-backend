package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.enums.*;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceAssertDO;
import org.alex.platform.pojo.InterfaceCaseDO;
import org.alex.platform.pojo.template.ImportCaseTemplate;
import org.alex.platform.service.InterfaceAssertService;
import org.alex.platform.service.InterfaceCaseImportService;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.util.ExceptionUtil;
import org.alex.platform.util.FileUtil;
import org.alex.platform.util.NoUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class InterfaceCaseImportServiceImpl implements InterfaceCaseImportService {
    @Autowired
    InterfaceCaseService interfaceCaseService;
    InterfaceAssertService assertService;
    LoginUserInfo userInfo;

    private static final Logger LOG = LoggerFactory.getLogger(InterfaceCaseImportServiceImpl.class);

    @Override
    @SuppressWarnings({"rawtypes"})
    public HashMap<String, Integer> importCase(MultipartFile file, HttpServletRequest request) throws BusinessException {
        String creator = userInfo.getRealName(request);
        String importNum = NoUtil.genIfImportNo();

        int totalNum = 0;
        int successNum = 0;
        int failedNum = 0;

        InputStream is;
        FileInputStream fis;

        LOG.info("开始文件导入流程");

        try {
            is= file.getInputStream();
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            throw new BusinessException("文件上传异常");
        }
        if (is instanceof FileInputStream) {
            fis = (FileInputStream)is;
        } else {
            throw new BusinessException("excel文件解析异常");
        }
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new BusinessException("excel文件解析异常");
        }
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        if (type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("xlsx")) {
            LOG.info("导入方式：excel");
            ArrayList<List> result;
            try {
                if (type.equalsIgnoreCase("xls")) {
                    result = FileUtil.readExcel(fis, ExcelType.XLS);
                } else {
                    result = FileUtil.readExcel(fis, ExcelType.XLSX);
                }
            } catch (Exception e) {
                LOG.error(ExceptionUtil.msg(e));
                throw new BusinessException("excel文件读取异常");
            }
            totalNum = result.size();
            LOG.info("总行数：{}", totalNum);
            LOG.info("开始按行读取并新增");
            for (int i = 0; i < result.size(); i++) {
                try {
                    Integer caseId = insertCaseByOffice(result.get(i), creator, (byte) 1, importNum);
                    successNum++;
                    LOG.info("新增成功，当前索引值：{}，用例编号：{}", i, caseId);
                } catch (Exception e) {
                    failedNum++;
                    LOG.error("新增失败，当前索引值：{}", i);
                    LOG.error(ExceptionUtil.msg(e));
                }
            }
        } else if (type.equalsIgnoreCase("csv")) {
            LOG.info("导入方式：csv");
            ArrayList<List<String>> result;
            try {
                result = FileUtil.readCsv(fis, true, StandardCharsets.ISO_8859_1);
            } catch (Exception e) {
                LOG.error(ExceptionUtil.msg(e));
                throw new BusinessException("csv文件读取异常");
            }
            totalNum = result.size();
            LOG.info("总行数：{}", totalNum);
            LOG.info("开始按行读取并新增");
            for (int i = 0; i < result.size(); i++) {
                try {
                    Integer caseId = insertCaseByOffice(result.get(i), creator, (byte) 2, importNum);
                    successNum++;
                    LOG.info("新增成功，当前索引值：{}，用例编号：{}", i, caseId);
                } catch (Exception e) {
                    failedNum++;
                    LOG.error("新增失败，当前索引值：{}", i);
                    LOG.error(ExceptionUtil.msg(e));
                }
            }
        } else if (type.equalsIgnoreCase("json")) {
            LOG.info("导入方式：json");
            JSONArray caseArray;
            String fileContent = FileUtil.readByBuffer(fis, StandardCharsets.UTF_8);
            try {
                caseArray = JSON.parseArray(fileContent);
            } catch (Exception e) {
                LOG.error(ExceptionUtil.msg(e));
                throw new BusinessException("json文件读取异常");
            }
            totalNum = caseArray.size();
            LOG.info("总行数：{}", totalNum);
            LOG.info("开始解析json对象并新增");
            for (int i = 0; i < caseArray.size(); i++) {
                try {
                    Integer caseId = insertCaseByJson(caseArray.getJSONObject(i), creator, importNum);
                    successNum++;
                    LOG.info("新增成功，当前索引值：{}，用例编号：{}", i, caseId);
                } catch (Exception e) {
                    failedNum++;
                    LOG.error("新增失败，当前索引值：{}", i);
                    LOG.error(ExceptionUtil.msg(e));
                }
            }
        } else if (type.equalsIgnoreCase("yaml")) {
            LOG.info("导入方式：yaml");
            Yaml yaml = new Yaml();
        } else {
            throw new BusinessException("不支持此类型文件");
        }
        LOG.info("用例导入流程完成，总记录：{}，成功：{}，失败：{}", totalNum, successNum, failedNum);
        return importResult(totalNum, successNum, failedNum);
    }


    /**
     * 根据excel、csv导入插入用例
     * @param row excel、csv row数据
     * @param creator 创建人
     * @param source 来源 1excel 2csv
     * @param importNum 导入编号
     * @return 自增用例编号
     * @throws BusinessException 业务异常
     */
    @SuppressWarnings({"rawtypes"})
    private Integer insertCaseByOffice(List row, String creator, Byte source, String importNum) throws BusinessException {
        ImportCaseTemplate template = new ImportCaseTemplate();
        Date date = new Date();

        int projectId;
        int moduleId;
        byte method;
        String url;
        String desc;
        byte level;
        String doc;
        String headers;
        String params;
        String data;
        String json;
        String assertStr;

        if (source == 1) { //来源 1excel 2csv
            projectId = ((Double) row.get(0)).intValue();
            moduleId = ((Double) row.get(1)).intValue();
        } else {
            projectId = Integer.parseInt((String)row.get(0));
            moduleId = Integer.parseInt((String)row.get(1));
        }
        method = method2key((String)row.get(2));
        url = (String)row.get(3);
        desc = (String)row.get(4);
        level = level2key((String)row.get(5));
        doc = (String)row.get(6);
        headers = (String)row.get(7);
        params = (String)row.get(8);
        data = (String)row.get(9);
        json = (String)row.get(10);
        assertStr = (String)row.get(11);

        // 插入用例主表
        template.setProjectId(projectId);
        template.setModuleId(moduleId);
        template.setUrl(url);
        template.setMethod(method);
        template.setDesc(desc);
        template.setLevel(level);
        template.setDoc(doc);
        template.setHeaders(headers);
        template.setParams(params);
        template.setData(data);
        template.setJson(json);
        template.setCreater(creator);
        template.setCreatedTime(date);
        template.setUpdateTime(date);
        template.setSource(source);
        template.setImportNo(importNum);
        InterfaceCaseDO interfaceCaseDO = interfaceCaseService.saveInterfaceCase(template);
        Integer caseId = interfaceCaseDO.getCaseId();

        //解析断言并插入断言表
        JSONArray assertArray = JSONArray.parseArray(assertStr);
        insertAssertArray(assertArray, caseId, date);
        return caseId;
    }

    /**
     * 插入断言表
     * @param assertArray 断言数组
     * @param caseId 用例编号
     * @param insertTime 新增时间
     * @throws BusinessException 业务异常
     */
    private void insertAssertArray(JSONArray assertArray, Integer caseId, Date insertTime) throws BusinessException {
        if (assertArray != null && !assertArray.isEmpty()) {
            for (int i = 0; i < assertArray.size(); i++) {
                JSONObject assertObject = assertArray.getJSONObject(i);
                if (assertObject != null) {
                    InterfaceAssertDO assertDO = new InterfaceAssertDO();
                    Integer order = assertObject.getInteger("order");
                    String assertName = assertObject.getString("assertName");
                    Byte type = assertType2key(assertObject.getString("type"));
                    String expression = assertObject.getString("expression");
                    Byte operator = assertOperator2key(assertObject.getString("operator"));
                    String exceptedResult = assertObject.getString("exceptedResult");
                    assertDO.setAssertName(assertName);
                    assertDO.setCaseId(caseId);
                    assertDO.setType(type);
                    assertDO.setExpression(expression);
                    assertDO.setOperator(operator);
                    assertDO.setExceptedResult(exceptedResult);
                    assertDO.setOrder(order);
                    assertDO.setCreatedTime(insertTime);
                    assertDO.setUpdateTime(insertTime);
                    assertService.saveAssert(assertDO);
                }
            }
        }
    }

    /**
     * 根据excel导入插入用例
     * @param jsonObject json对象
     * @param creator 创建人
     * @param importNum 导入编号
     * @return 自增用例编号
     * @throws BusinessException 业务异常
     */
    private Integer insertCaseByJson(JSONObject jsonObject, String creator, String importNum)
            throws BusinessException {

        ImportCaseTemplate template = new ImportCaseTemplate();
        Date date = new Date();

        Integer projectId = jsonObject.getInteger("projectId");
        Integer moduleId = jsonObject.getInteger("moduleId");
        String url = jsonObject.getString("url");
        Byte method = method2key(jsonObject.getString("method"));
        String desc = jsonObject.getString("desc");
        Byte level = level2key(jsonObject.getString("level"));
        String doc = jsonObject.getString("doc");
        String headers = jsonObject.getString("headers");
        String params = jsonObject.getString("params");
        String data = jsonObject.getString("data");
        String json = jsonObject.getString("json");

        // 插入用例主表
        template.setProjectId(projectId);
        template.setModuleId(moduleId);
        template.setUrl(url);
        template.setMethod(method);
        template.setDesc(desc);
        template.setLevel(level);
        template.setDoc(doc);
        template.setHeaders(headers);
        template.setParams(params);
        template.setData(data);
        template.setJson(json);
        template.setCreater(creator);
        template.setCreatedTime(date);
        template.setUpdateTime(date);
        template.setSource((byte) 3);
        template.setImportNo(importNum);
        InterfaceCaseDO interfaceCaseDO = interfaceCaseService.saveInterfaceCase(template);
        Integer caseId = interfaceCaseDO.getCaseId();

        // 插入断言表
        JSONArray assertArray = jsonObject.getJSONArray("asserts");
        insertAssertArray(assertArray, caseId, date);

        return caseId;
    }

    /**
     * 导入结果封装
     * @param totalNum 总数
     * @param successNum 成功数
     * @param failedNum 失败数
     * @return HashMap
     */
    private HashMap<String, Integer> importResult(Integer totalNum, Integer successNum, Integer failedNum){
        HashMap<String, Integer> result = new HashMap<>();
        result.put("total", totalNum);
        result.put("success", successNum);
        result.put("failed", failedNum);
        return result;
    }

    /**
     * method string 转对应的码值
     * @param methodStr 方法名称
     * @return 对应方法名称的码值 未匹配默认get
     */
    private byte method2key(String methodStr) {
        if (StringUtils.isEmpty(methodStr)) {
            return 0;
        }
        return CaseMethod.getMethodKey(methodStr);
    }

    /**
     * 用例级别转对应的码值
     * @param levelStr 用例级别
     * @return 对应用例等级的码值 未匹配默认中
     */
    private byte level2key(String levelStr) {
        if (StringUtils.isEmpty(levelStr)) {
            return 0;
        }
        return CaseLevel.getLevelKey(levelStr);
    }

    /**
     * assert type 转对应的码值
     * @param assertTypeStr 断言类型字符串
     * @return 对应断言类型的码值 未匹配默认json
     */
    private byte assertType2key(String assertTypeStr) {
        if (StringUtils.isEmpty(assertTypeStr)) {
            return 0;
        }
        return AssertType.getAssertTypeKey(assertTypeStr);
    }

    /**
     * assert operator 转对应的码值
     * @param assertOperatorStr 断言比较符
     * @return 对应断言比较符的码值 未匹配返回=
     */
    private byte assertOperator2key(String assertOperatorStr) {
        if (StringUtils.isEmpty(assertOperatorStr)) {
            return 0;
        }
        return AssertOperator.getAssertOperatorKey(assertOperatorStr);
    }
}
