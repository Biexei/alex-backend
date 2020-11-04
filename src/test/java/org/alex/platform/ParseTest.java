package org.alex.platform;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.service.*;
import org.alex.platform.util.AssertUtil;
import org.alex.platform.util.ParseUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
//        System.out.println(ParseUtil.parseJson(json, "$..store.book[0].category"));
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

    public void parsePostProcessor(String s) {
        Pattern pattern = Pattern.compile("#\\{.+?\\}");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            String postProcessorName = findStr.substring(2, findStr.length() - 1);
            System.out.println(postProcessorName);
        }
    }

    @Test
    public void testParse() {
        String s = "#{a}#{b}#{c#{#d}}";
        parsePostProcessor(s);
    }

}
