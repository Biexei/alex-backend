package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ImportCaseServiceImpl implements ImportCaseService {

    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceAssertService assertService;
    @Autowired
    LoginUserInfo userInfo;

    /**
     * 根据json/yaml导入插入用例
     * @param jsonObject json对象
     * @param projectId 项目编号
     * @param moduleId 模块编号
     * @param creator 创建人
     * @param source 来源 3json 4yaml
     * @param importNum 导入编号
     * @return 自增用例编号
     */
    @Override
    public Integer insertCaseByJsonYaml(JSONObject jsonObject, Integer projectId, Integer moduleId, String creator, byte source, String importNum) throws BusinessException {

        ImportCaseTemplate template = new ImportCaseTemplate();
        Date date = new Date();

        String url = jsonObject.getString("url");
        Byte method = method2key(jsonObject.getString("method"));
        String desc = jsonObject.getString("desc");
        Byte level = level2key(jsonObject.getString("level"));
        String doc = jsonObject.getString("doc");
        String headers = kvCast(jsonObject.getString("headers"));
        String params = kvCast(jsonObject.getString("params"));
        String data = kvCast(jsonObject.getString("data"));
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
        if (data == null && json == null) {
            template.setBodyType((byte) 9); // none
            template.setRaw(null);
            template.setRawType(null);
        } else {
            if (json != null) {
                template.setBodyType((byte) 2); // raw
                template.setRaw(json);
                template.setRawType("JSON");
            } else {
                template.setBodyType((byte) 1); // x-www-form-encoded
                template.setFormDataEncoded(data);
                template.setRaw(null);
                template.setRawType(null);
            }
        }
        template.setCreater(creator);
        template.setCreatedTime(date);
        template.setUpdateTime(date);
        template.setSource(source);
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
     * @param projectId 项目编号
     * @param moduleId 模块编号
     * @param creator 创建人
     * @param source 来源 1excel 2csv
     * @param importNum 导入编号
     * @return 自增用例编号
     * @throws BusinessException 业务异常
     */
    @SuppressWarnings({"rawtypes"})
    @Override
    public Integer insertCaseByOffice(List row, Integer projectId, Integer moduleId, String creator, byte source, String importNum) throws BusinessException {
        ImportCaseTemplate template = new ImportCaseTemplate();
        Date date = new Date();

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

        method = method2key((String)row.get(2));
        url = (String)row.get(3);
        desc = (String)row.get(4);
        level = level2key((String)row.get(5));
        doc = (String)row.get(6);
        headers = kvCast((String)row.get(7));
        params = kvCast((String)row.get(8));
        data = kvCast((String)row.get(9));
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
        if (data == null && json == null) {
            template.setBodyType((byte) 9); // none
            template.setRaw(null);
            template.setRawType(null);
        } else {
            if (json != null) {
                template.setBodyType((byte) 2); // raw
                template.setRaw(json);
                template.setRawType("JSON");
            } else {
                template.setBodyType((byte) 1); // x-www-form-encoded
                template.setFormDataEncoded(data);
                template.setRaw(null);
                template.setRawType(null);
            }
        }
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
     * 根据har导入用例
     * @param entry har文件的entries数组中的item
     * @param projectId 项目编号
     * @param moduleId 模块编号
     * @param creator 创建人
     * @param importNum 导入编号
     * @return 自增用例编号
     * @throws BusinessException 业务异常
     */
    @Override
    public Integer insertCaseByHar(JSONObject entry, Integer projectId, Integer moduleId, String creator, String importNum) throws BusinessException {
        JSONObject request = entry.getJSONObject("request");
        JSONObject postData = request.getJSONObject("postData");

        JSONArray params = postData.getJSONArray("params");
        String mimeType = postData.getString("mimeType").toLowerCase();
        String text = postData.getString("text");

        JSONArray queryString = request.getJSONArray("queryString");
        JSONArray headers = request.getJSONArray("headers");
        String url = request.getString("url");
        String method = request.getString("method");

        // 新增测试用例主表
        InterfaceCaseDO template = new InterfaceCaseDO();
        template.setProjectId(projectId);
        template.setModuleId(moduleId);
        template.setUrl(URI.create(url).getPath());
        template.setMethod(this.method2key(method));
        template.setDesc("");
        template.setLevel((byte)0);
        template.setDoc(null);
        template.setHeaders(headers.isEmpty() ? null : this.kvCast(headers));
        template.setParams(queryString.isEmpty() ? null : this.kvCast(queryString));
        // 0form-data 1x-www-form-Encoded 2raw 9none
        if (mimeType.isEmpty()) {
            template.setFormData(null);
            template.setFormDataEncoded(null);
            template.setBodyType((byte)9);
            template.setRaw(null);
            template.setRawType(null);
         // multipart/form-data不处理，因为har导出来的格式不是k&v
//        } else if (mimeType.contains("multipart/form-data")) {
//            template.setFormData(this.kvCast(params));
//            template.setFormDataEncoded(null);
//            template.setBodyType((byte)0);
//            template.setRaw(null);
//            template.setRawType(null);
        } else if (mimeType.contains("application/x-www-form-urlencoded")) {
            template.setFormData(null);
            template.setFormDataEncoded(this.kvCast(params));
            template.setBodyType((byte)1);
            template.setRaw(null);
            template.setRawType(null);
        } else if (mimeType.contains("application/json")) {
            template.setFormData(null);
            template.setFormDataEncoded(null);
            template.setBodyType((byte)2);
            template.setRaw(text);
            template.setRawType("JSON");
        } else if (mimeType.contains("text/html")) {
            template.setFormData(null);
            template.setFormDataEncoded(null);
            template.setBodyType((byte)2);
            template.setRaw(text);
            template.setRawType("HTML");
        } else if (mimeType.contains("text/xml")) {
            template.setFormData(null);
            template.setFormDataEncoded(null);
            template.setBodyType((byte)2);
            template.setRaw(text);
            template.setRawType("XML");
        } else {
            template.setFormData(null);
            template.setFormDataEncoded(null);
            template.setBodyType((byte)2);
            template.setRaw(text);
            template.setRawType("Text");
        }
        template.setCreater(creator);
        Date date = new Date();
        template.setCreatedTime(date);
        template.setUpdateTime(date);
        template.setSource((byte)5);
        template.setImportNo(importNum);
        InterfaceCaseDO interfaceCaseDO = interfaceCaseService.saveInterfaceCase(template);
        return interfaceCaseDO.getCaseId();
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

    /**
     * 将name、value对象转成name、value、checked、desc的对象数组
     * @param text header、param、form-data、form-data-encoded
     * @return 转换结果
     */
    private String kvCast(String text) throws BusinessException {
        try {
            if (text != null && !text.isEmpty()) {
                JSONArray array = new JSONArray();
                JSONObject object = JSON.parseObject(text);
                for (Map.Entry<String, Object> entry : object.entrySet()) {
                    JSONObject var1 = new JSONObject();
                    var1.put("name", entry.getKey());
                    var1.put("value", entry.getValue() == null ? null : entry.getValue().toString());
                    var1.put("checked", true);
                    var1.put("desc", null);
                    array.add(var1);
                }
                return JSON.toJSONString(array, SerializerFeature.WriteMapNullValue);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new BusinessException("headers/params/form-data/form-data-encoded格式错误");
        }
    }

    /**
     * har的postData.params、queryString、headers转换
     * @param a postData.params、queryString、headers
     * @return 转换结果
     */
    private String kvCast(JSONArray a) throws BusinessException {
        try {
            if (a != null && !a.isEmpty()) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < a.size(); i++) {
                    JSONObject object = a.getJSONObject(i);
                    // object自带name、value
                    object.put("checked", true);
                    object.put("desc", null);
                    array.add(object);
                }
                return JSON.toJSONString(array, SerializerFeature.WriteMapNullValue);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new BusinessException("har格式错误");
        }
    }
}
