package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.enums.AssertOperator;
import org.alex.platform.enums.AssertType;
import org.alex.platform.enums.CaseLevel;
import org.alex.platform.enums.CaseMethod;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceAssertDO;
import org.alex.platform.pojo.InterfaceCaseDO;
import org.alex.platform.pojo.template.ImportCaseTemplate;
import org.alex.platform.service.ImportCaseService;
import org.alex.platform.service.InterfaceAssertService;
import org.alex.platform.service.InterfaceCaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ImportCaseServiceImpl implements ImportCaseService {

    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceAssertService assertService;
    @Autowired
    LoginUserInfo userInfo;

    /**
     * 根据excel导入插入用例
     * @param jsonObject json对象
     * @param creator 创建人
     * @param importNum 导入编号
     * @return 自增用例编号
     */
    @Override
    public Integer insertCaseByJson(JSONObject jsonObject, String creator, String importNum) throws BusinessException {

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
        if (data != null && data.isEmpty()) {
            data = null;
        }
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
     * 根据excel、csv导入插入用例
     * @param row excel、csv row数据
     * @param creator 创建人
     * @param source 来源 1excel 2csv
     * @param importNum 导入编号
     * @return 自增用例编号
     * @throws BusinessException 业务异常
     */
    @SuppressWarnings({"rawtypes"})
    @Override
    public Integer insertCaseByOffice(List row, String creator, Byte source, String importNum) throws BusinessException {
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
        if (data != null && data.isEmpty()) {
            data = null;
        }
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
                    String assertName = assertObject.getString("desc");
                    Byte type = assertType2key(assertObject.getString("type"));
                    String expression = assertObject.getString("expression");
                    Byte operator = assertOperator2key(assertObject.getString("operator"));
                    String exceptedResult = assertObject.getString("expect");
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
