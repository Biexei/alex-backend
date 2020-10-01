package org.alex.platform;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.*;
import org.alex.platform.util.JdbcUtil;
import org.alex.platform.util.ParseUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JDBCTest {
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;
    @Autowired
    ModuleMapper moduleMapper;
    @Autowired
    InterfaceAssertService interfaceAssertService;
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;
    @Autowired
    ProjectService projectService;
    @Autowired
    InterfaceAssertLogService assertLogService;
    @Autowired
    InterfaceCaseRelyDataService ifCaseRelyDataService;
    @Autowired
    RelyDataService relyDataService;
    @Autowired
    DbService dbService;
    @Test
    public void testConn() throws ParseException {
        String url = "jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false";
        String username = "root";
        String password = "root";
        JdbcTemplate jdbc = JdbcUtil.getInstance(url, username, password);
        List<Map<String, Object>> query = jdbc.queryForList("select username from `t_user` where user_id= ?",18);
//        System.out.println(this.getValue(query, 0, 0));
//        for (Object key : result.keySet()) {
//            System.out.println(result.get(key));
//            break;
//        }
    }

    public String getValue(List<Map<String, Object>> queryResult, int rowIndex, int colIndex) throws ParseException {
        if (rowIndex >= queryResult.size()) {
            throw new ParseException("请重新确定行大小");
        }
        Map<String, Object> map = queryResult.get(rowIndex);
        if (colIndex >= map.size()) {
            throw new ParseException("请重新确定列大小");
        }
        String result = "";
        int index = 0;
        for (Map.Entry entry :
             map.entrySet()) {
            if (index == colIndex) {
                result = entry.getValue().toString();
                break;
            }
            index ++;
        }
        return result;
    }

    public String parseRelyData(String s) throws ParseException, BusinessException, SqlException {
        Pattern p = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            // 获取relyName
            String relyName = findStr.substring(2, findStr.length() - 1);
            System.out.println("----------------------------");
            System.out.println(relyName);
// 进入普通依赖数据模式-再进入根据数组下标模式
            if (relyName.indexOf("[") != -1 && relyName.endsWith("]")) {
                System.out.println("进入普通依赖数据模式-再进入根据数组下标模式");
                // 判断出现次数,首次出现和最后一次出现位置不一致，则说明[>1 ]>1
                if (relyName.indexOf("[") != relyName.lastIndexOf("[") ||
                        relyName.indexOf("]") != relyName.lastIndexOf("]")) {
                    throw new ParseException("数组取值语法错误");
                }
                String indexStr = relyName.substring(relyName.indexOf("[") + 1, relyName.length() - 1);
                try {
                    int index = Integer.parseInt(indexStr);
                    relyName = relyName.substring(0, relyName.indexOf("["));
                    // 查询其所依赖的caseId
                    InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                    interfaceCaseRelyDataDTO.setRelyName(relyName);
                    InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                    if (null == interfaceCaseRelyDataVO) {
                        throw new ParseException("未找到该依赖数值");
                    }
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    // 根据caseId调用相应case
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(caseId);
                    // 获取case执行结果, 不等于0, 则用例未通过
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        throw new BusinessException("relyName关联的前置用例执行失败!");
                    }
                    // 根据executeLogId查询对应的执行记录
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
                    // 2020.09.27 xpath/jsonPath也支持下标
//                    if (contentType != 2) {
//                        throw new ParseException("只有依赖数据提取类型为header时才支持指定下标，" +
//                                "否则请自行调整jsonpath/xpath表达式，使提取结果唯一");
//                    }
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                throw new ParseException(expression + "提取内容为空");
                            }
                            try {
                                s = s.replace(findStr, jsonPathArray.get(index).toString());
                            } catch (Exception e) {
                                throw new ParseException(relyName + " 数组下标越界");
                            }
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            if (xpathArray.isEmpty()) {
                                throw new ParseException(expression + "提取内容为空");
                            }
                            try {
                                s = s.replace(findStr, xpathArray.get(index).toString());
                            } catch (Exception e) {
                                throw new ParseException(relyName + " 数组下标越界");
                            }
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(expression);
                            if (null == headerArray) {
                                throw new ParseException("未找到请求头:" + expression);
                            }
                            try {
                                s = s.replace(findStr, headerArray.get(index).toString());
                            } catch (Exception e) {
                                throw new ParseException(expression + " 数组下标越界");
                            }
                        } else {
                            throw new BusinessException("不支持该contentType");
                        }
                    } catch (BusinessException e) {
                        throw new BusinessException("不支持该contentType");
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ParseException(e.getMessage());
                    }
                } catch (NumberFormatException e) {
                    throw new ParseException("数组下标只能为数字");
                }
// 进入预置函数模式
            } else if (relyName.indexOf("(") != -1 && relyName.endsWith(")")) {
                System.out.println("进入预置函数模式");
                // 判断出现次数,首次出现和最后一次出现位置不一致，则说明(>1 )>1
                if (relyName.indexOf("(") != relyName.lastIndexOf("(") ||
                        relyName.indexOf(")") != relyName.lastIndexOf(")")) {
                    throw new ParseException("依赖函数语法错误");
                }
                // 获取方法名称
                String methodName = relyName.substring(0, relyName.indexOf("("));
                RelyDataVO relyDataVO = relyDataService.findRelyDataByName(methodName);
                // 获取参数列表, 去除引号空格
                String[] params = relyName.substring(relyName.indexOf("(") + 1, relyName.length() - 1)
                        .replace("\"", "").replaceAll(",\\s+", ",")
                        .replace("'", "").split(",");
                if (null == relyDataVO) {
                    throw new ParseException("未找到该依赖方法");
                }
                // 无参方法特殊处理
                if (params.length == 1 && "".equals(params[0])) {
                    params = new String[0];
                }
                // 反射执行对应方法
                try {
                    Class<?> clazz = Class.forName("org.alex.platform.common.RelyMethod");
                    Class[] paramsList = new Class[params.length];
                    for (int i = 0; i < params.length; i++) {
                        paramsList[i] = String.class;
                    }
                    Method method = clazz.getMethod(methodName, paramsList);
                    s = s.replace(findStr, (String) method.invoke(clazz.newInstance(), params));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ParseException("未找到依赖方法或者入参错误");
                }
// 进入普通依赖数据模式
            } else {
                System.out.println("进入普通依赖数据模式");
                // 查询其所依赖的caseId
                InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                interfaceCaseRelyDataDTO.setRelyName(relyName);
                InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                // 判断是否在t_interface_case_rely_data
                if (null == interfaceCaseRelyDataVO) {
                    RelyDataVO relyDataVO = relyDataService.findRelyDataByName(relyName);
                    // 判断是否在t_rely_data
                    if (null == relyDataVO) {
                        throw new ParseException("未找到该依赖数值");
                    } else {
                        // 此处不考虑反射函数类型，已经在${xx()}步骤处理
                        // 依赖类型 0固定值 1反射方法 2sql
                        int type = relyDataVO.getType();
                        if (type == 0) {
                            s = s.replace(findStr, relyDataVO.getValue());
                        } else if (type == 2) {
                            Integer datasourceId = relyDataVO.getDatasourceId();
                            if (null == datasourceId) {
                                throw new ParseException("sql未找到对应的数据源");
                            }
                            DbVO dbVO = dbService.findDbById(datasourceId);
                            // 0启动 1禁用
                            int status = dbVO.getStatus();
                            if (status == 1) {
                                throw new ParseException("数据源已被禁用");
                            }
                            String url = dbVO.getUrl();
                            String username = dbVO.getUsername();
                            String password = dbVO.getPassword();
                            // 支持动态sql
                            String sql = relyDataVO.getValue();
                            if (relyDataVO.getValue() != null ){
                                sql = parseRelyData(sql);
                            }
                            String sqlResult = JdbcUtil.selectFirstColumn(url, username, password, sql);
                            s = s.replace(findStr, sqlResult);
                        }
                    }
                } else {
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    // 根据caseId调用相应case
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(caseId);
                    // 获取case执行结果, 不等于0, 则用例未通过
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        throw new BusinessException("relyName关联的前置用例执行失败!");
                    }
                    // 根据executeLogId查询对应的执行记录
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                throw new ParseException(expression + "提取内容为空");
                            }
                            s = s.replace(findStr, jsonPathArray.get(0).toString());
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            if (xpathArray.isEmpty()) {
                                throw new ParseException(expression + "提取内容为空");
                            }
                            s = s.replace(findStr, xpathArray.get(0).toString());
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders,
                                    HashMap.class).get(expression);
                            if (headerArray == null) {
                                throw new ParseException("未找到请求头:" + expression);
                            } else {
                                s = s.replace(findStr, headerArray.get(0).toString());
                            }
                        } else {
                            throw new BusinessException("不支持该contentType");
                        }
                    } catch (BusinessException e) {
                        throw new BusinessException("不支持该contentType");
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ParseException(e.getMessage());
                    }
                }
            }
        }
        return s;
    }
}
