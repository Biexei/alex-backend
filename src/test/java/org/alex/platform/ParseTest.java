package org.alex.platform;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.util.AssertUtil;
import org.alex.platform.util.ParseUtil;
import org.alex.platform.util.RestUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParseTest {
    @Test
    @Ignore
    public void testParseXml() throws ParseException {
        String xml =
                "<bookstore> \n" +
                        "  <book> \n" +
                        "    <title lang=\"USA\">Harry Potter1</title>  \n" +
                        "    <author>J K. Rowling1</author>  \n" +
                        "    <year name=\"1\">20051</year>  \n" +
                        "    <price>1000</price> \n" +
                        "  </book>  \n" +
                        "  <book> \n" +
                        "    <title lang=\"cn\">Harry Potter2</title>  \n" +
                        "    <author>J K. Rowling2</author>  \n" +
                        "    <year name=\"1\">20053</year>  \n" +
                        "    <price>29.992</price> \n" +
                        "  </book>  \n" +
                        "  <book> \n" +
                        "    <title lang=\"zn\">Harry Potter3</title>  \n" +
                        "    <author>J K. Rowling3</author>  \n" +
                        "    <year>20053</year>  \n" +
                        "    <price>29.993</price> \n" +
                        "  </book> \n" +
                        "</bookstore>";
        String xpath = "//title[@lang='zn']";
        System.out.println(ParseUtil.parseXml(xml, xpath));
    }

    @Test
    @Ignore
    public void testParseHeader() throws Exception {
        System.out.println(ParseUtil.parseHttpHeader(RestUtil.get("http://www.meidekan.com/"), "Set-Cookie"));
    }

    @Test()
    @Ignore
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
        System.out.println(ParseUtil.parseJson(json, "$..store11"));
    }

    @Test
    @Ignore
    public void testGet() throws BusinessException {
        String url = "http://www.baidu.com/{name}/{class}/";
        HashMap<String, String> map = new HashMap<>();
        map.put("class", "126");
        map.put("sex", "nan");
        map.put("name", "123");
        RestUtil.get(url, map, map);
//        Pattern p = Pattern.compile("\\{(\\w)+\\}");
//        Matcher m = p.matcher(url);
//        while (m.find()) {
//            System.out.println(m.group());
//        }
    }

    @Test
    public void testAssert() throws BusinessException{
        System.out.println(AssertUtil.asserts("12345a", 0, "12345a"));
    }

}
