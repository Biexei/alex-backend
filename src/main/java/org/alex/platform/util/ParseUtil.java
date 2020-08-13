package org.alex.platform.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import org.alex.platform.exception.ParseException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParseUtil {
    /**
     * xpath解析
     *
     * @param xmlText
     * @param xpath
     * @return
     * @throws ParseException
     */
    public static String parseXml(String xmlText, String xpath) throws ParseException {
        List<String> result = new ArrayList<>();
        try {
            Document document = new SAXReader().read(new StringReader(xmlText));
            List<Node> nodes = document.selectNodes(xpath);
            for (Node node : nodes) {
                result.add(node.getText());
            }
        } catch (DocumentException e) {
            throw new ParseException("xml格式错误");
        } catch (Exception e) {
            throw new ParseException("未找到该元素，请确保xpath无误");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 解析http状态码
     *
     * @param entity
     * @return
     */
    public static int parseHttpCode(ResponseEntity entity) {
        return entity.getStatusCodeValue();
    }

    /**
     * 解析请求头
     * @param entity
     * @param headerName
     * @return
     * @throws ParseException
     */
    public static String parseHttpHeader(ResponseEntity entity, String headerName) throws ParseException {
        HttpHeaders headerMap = entity.getHeaders();
        if (!headerMap.containsKey(headerName)) {
            throw new ParseException("响应头中未找到该元素，请确保header无误");
        }
        // 若header长度为1则返回string to json字符串
        if (entity.getHeaders().get(headerName).size() == 1) {
            return JSON.toJSONString(entity.getHeaders().get(headerName).get(0));
        } else { //若header长度为1则返回array to json字符串
            return JSON.toJSONString(entity.getHeaders().get(headerName));
        }
    }

    /**
     * 解析json
     * @param jsonText
     * @param jsonPath
     * @return
     */
    public static String parseJson(String jsonText, String jsonPath) {
        return JSON.toJSONString(JsonPath.read(jsonText, jsonPath));
    }
}
