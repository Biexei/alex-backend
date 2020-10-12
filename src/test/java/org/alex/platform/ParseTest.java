package org.alex.platform;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.common.RelyMethod;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.*;
import org.alex.platform.util.AssertUtil;
import org.alex.platform.util.JdbcUtil;
import org.alex.platform.util.ParseUtil;
import org.alex.platform.util.RestUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParseTest {
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
    public void testParseXml() throws ParseException {
        String xml = "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <meta http-equiv=\"Cache-Control\" content=\"no-transform\" />\n" +
                "    <meta http-equiv=\"Cache-Control\" content=\"no-siteapp\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\n" +
                "    <link rel=\"stylesheet\" href=\"http://static.meidekan.com/css/404.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"header\">\n" +
                "    <div class=\"indexwidth\">\n" +
                "        <a target=\"_blank\" title=\"美德网\" href=\"http://www.meidekan.com/\" class=\"logo\"></a>\n" +
                "        <div class=\"nav\">\n" +
                "            <ul>\n" +
                "                <li><a target=\"_blank\" id = \"id\"href=\"/\">首 页</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/yuwen/\" title=\"语文\">语文</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/meiwen/\" title=\"美文\">美文</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/zuowen/\" title=\"作文\">作文</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/wenxue/\" title=\"文学\">文学</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/gushiwen/\" title=\"古诗文\">古诗文</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/shiyongwen/\" title=\"实用文\">实用文</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/shiti/\" title=\"试题\">试题</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/jiaoan/\" title=\"教案\">教案</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/kejian/\" title=\"课件\">课件</a></li>\n" +
                "                <li><a target=\"_blank\" href=\"/sucai/\" title=\"素材\">素材</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "        <div class=\"search\">\n" +
                "            <form action=\"#\" onsubmit=\"window.open('http'+'://so.meidekan.com/cse/search?s='+this.elements.s.value+'&q='+this.elements.q.value);return false;\" method=\"get\" target=\"_blank\">\n" +
                "                <input type=\"hidden\" name=\"s\" value=\"17870499895628785359\">\n" +
                "                <input type=\"text\" name=\"q\" placeholder=\"请输入关键词搜索\" class=\"searchbar\">\n" +
                "                <input type=\"submit\" value=\"搜索\" class=\"search_results\">\n" +
                "            </form>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div class=\"w-index\">\n" +
                "    <div class=\"pageError\">\n" +
                "        <div class=\"number\">404<span>Error</span></div>\n" +
                "        <div class=\"pageError_right\">\n" +
                "            <p>您所访问的页面找不到了！</p>\n" +
                "            <span>\n" +
                "\t\t</span>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div class=\"footer\">\n" +
                "    <div class=\"foot_box\">\n" +
                "        <p>Copyright&copy;2006-2019 <a target=\"_blank\" title=\"美德网\" href=\"http://www.meidekan.com/\">美德网</a> meidekan.com版权所有</p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        String xpath = "//a[@id='id']/text()";
        System.out.println(ParseUtil.parseXml(xml, xpath));
    }

    @Test()
    public void testParaseJson() {
        String json = "\n" +
                "{\n" +
                "    \"store\": {\n" +
                "        \"book\": [\n" +
                "            {\n" +
                "                \"category\": \"reference\",\n" +
                "                \"author\": \"Nigel Rees\",\n" +
                "                \"title\": \"Sayings of the Century\",\n" +
                "                \"price\": 8.95\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"Evelyn Waugh\",\n" +
                "                \"title\": \"Sword of Honour\",\n" +
                "                \"price\": 12.99\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"Herman Melville\",\n" +
                "                \"title\": \"Moby Dick\",\n" +
                "                \"isbn\": \"0-553-21311-3\",\n" +
                "                \"price\": 8.99\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"J. R. R. Tolkien\",\n" +
                "                \"title\": \"The Lord of the Rings\",\n" +
                "                \"isbn\": \"0-395-19395-8\",\n" +
                "                \"price\": 22.99\n" +
                "            }\n" +
                "        ],\n" +
                "        \"bicycle\": {\n" +
                "            \"color\": \"red\",\n" +
                "            \"price\": 19.95\n" +
                "        }\n" +
                "    },\n" +
                "    \"expensive\": 10\n" +
                "}";
        String s = "";
        System.out.println(ParseUtil.parseJson(json, "$..store.book[0].category"));
    }

    @Test
    public void testAssert() throws BusinessException{
        System.out.println(AssertUtil.asserts("12345a", 0, "12345a"));
    }

    @Test
    public void testRE () {
        Pattern p = Pattern.compile("#\\{.+?\\}");
        Matcher matcher = p.matcher("[${selectProjectIdByName(\"#{$..data.name}\", \"#{$..data.desc}\")}]");
        while (matcher.find()) {
            String findStr = matcher.group();
            String relyName = findStr.substring(2, findStr.length()-1);
            System.out.println(relyName);
        }
    }

    @Test
    public void testP() throws ParseException, BusinessException, SqlException {
        interfaceCaseService.parseRelyData("asdjhajskd--${cookie}");
//        String s = "asdjhajskd--cookie[123]";
//        Pattern pp = Pattern.compile("\\[[0-9]+\\]");
//        Matcher mm = pp.matcher(s);
//        while (mm.find()) {
//            System.out.println(mm.group());
//        }
    }

    @Test
    public void testT() throws ParseException, BusinessException, SqlException {
        String s = "s123.654asdA1 ${resultcode} ${yesterday(yyyy-MM-dd HH:mm:ss)} ${cookie[1]}";
        System.out.println(interfaceCaseService.parseRelyData(s));
    }

    public String parseRelyData(String s) throws ParseException, BusinessException, SqlException {
        Pattern p = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            // 获取relyName
            String relyName = findStr.substring(2, findStr.length()-1);
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
                String indexStr = relyName.substring(relyName.indexOf("[")+1, relyName.length()-1);
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
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(caseId, "系统调度", null);
                    // 获取case执行结果, 不等于0, 则用例未通过
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0){
                        throw new BusinessException("relyName关联的前置用例执行失败!");
                    }
                    // 根据executeLogId查询对应的执行记录
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                    int contentType = (int)interfaceCaseRelyDataVO.getContentType();
                    if (contentType != 2) {
                        throw new ParseException("只有依赖数据提取类型为header时才支持指定下标，" +
                                "否则请自行调整jsonpath/xpath表达式，使提取结果唯一");
                    }
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            s = s.replace(findStr, (String)jsonPathArray.get(0));
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            s = s.replace(findStr, (String)xpathArray.get(0));
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(expression);
                            try {
                                s = s.replace(findStr, (String)headerArray.get(index));
                            } catch (Exception e) {
                                throw new ParseException("数组下标越界");
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
            } else if(relyName.indexOf("(") != -1 && relyName.endsWith(")")) {
                System.out.println("进入预置函数模式");
                // 判断出现次数,首次出现和最后一次出现位置不一致，则说明(>1 )>1
                if (relyName.indexOf("(") != relyName.lastIndexOf("(") ||
                        relyName.indexOf(")") != relyName.lastIndexOf(")")) {
                    throw new ParseException("依赖函数语法错误");
                }
                // 获取方法名称
                String methodName = relyName.substring(0, relyName.indexOf("("));
                RelyDataVO relyDataVO = relyDataService.findRelyDataByName(methodName);
                if ( null == relyDataVO) {
                    throw new ParseException("未找到该依赖方法");
                }
                // 获取参数列表, 去除引号空格
                String[] params = relyName.substring(relyName.indexOf("(")+1, relyName.length()-1)
                        .replace("\"", "").replaceAll(",\\s+", ",")
                        .replace("'", "").split(",");
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
                    s = s.replace(findStr, (String)method.invoke(clazz.newInstance(), params));
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
                            if (null == datasourceId){
                                throw new ParseException("sql未找到对应的数据源");
                            }
                            DbVO dbVO = dbService.findDbById(datasourceId);
                            String url = dbVO.getUrl();
                            String username = dbVO.getUsername();
                            String password = dbVO.getPassword();
                            // 支持动态sql
                            String sql = parseRelyData(relyDataVO.getValue());
                            String sqlResult = JdbcUtil.selectFirstColumn(url, username, password, sql);
                            s = s.replace(findStr, sqlResult);
                        }
                    }
                } else {
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    // 根据caseId调用相应case
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(caseId, "系统调度", "null");
                    // 获取case执行结果, 不等于0, 则用例未通过
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0){
                        throw new BusinessException("relyName关联的前置用例执行失败!");
                    }
                    // 根据executeLogId查询对应的执行记录
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                    int contentType = (int)interfaceCaseRelyDataVO.getContentType();
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            s = s.replace(findStr, (String)jsonPathArray.get(0));
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            s = s.replace(findStr, (String)xpathArray.get(0));
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders,
                                    HashMap.class).get(expression);
                            s = s.replace(findStr, (String)headerArray.get(0));
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

    @Test
    public void doTest() throws ParseException, BusinessException, SqlException {
        System.out.println(this.parseRelyData("${username} ${time(M, -1, yyyy-MM-dd HH:mm:ss)} ${city}"));
    }
}
