package org.alex.platform.util;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import org.alex.platform.exception.ParseException;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes"})
public class ParseUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ParseUtil.class);


    /**
     * xpath解析
     *
     * @param xmlText  xml string
     * @param xpath xpath
     * @return 提取表达式
     * @throws ParseException 解析异常
     */
    public static String parseXml(String xmlText, String xpath) throws ParseException {
        try {
            JXDocument document = JXDocument.create(xmlText);
            List<JXNode> nodes = document.selN(xpath);
            List<String> result = new ArrayList();
            for (JXNode node : nodes) {
                result.add(node.toString());
            }
            return JSON.toJSONString(result);
        } catch (Exception e) {
            LOG.error("xpath解析异常，xml={}，xpath={}", xmlText, xpath);
            throw new ParseException("XPATH解析异常，xpath: " + xpath);
        }

//        List<String> result = new ArrayList<>();
//        try {
//            Document document = new SAXReader().read(new StringReader(xmlText));
//            List<Node> nodes = document.selectNodes(xpath);
//            for (Node node : nodes) {
//                result.add(node.getText());
//            }
//        } catch (DocumentException e) {
//            throw new ParseException("xml格式错误");
//        } catch (Exception e) {
//            throw new ParseException("未找到该元素，请确保xpath无误");
//        }
//        return JSON.toJSONString(result);
    }

    /**
     * 解析http状态码
     *
     * @param entity ResponseEntity
     * @return http 状态码
     */
    public static int parseHttpCode(ResponseEntity entity) {
        return entity.getStatusCodeValue();
    }

    /**
     * 解析请求头
     *
     * @param entity ResponseEntity
     * @param headerName headerName
     * @return 请求头
     * @throws ParseException 解析异常
     */
    public static String parseHttpHeader(ResponseEntity entity, String headerName) throws ParseException {
        HttpHeaders headerMap = entity.getHeaders();
        if (!headerMap.containsKey(headerName)) {
            LOG.error("解析响应头， 未找到该headerName， headerName={}", headerName);
            throw new ParseException("响应头中未找到该元素，请确保header无误");
        }
        return JSON.toJSONString(entity.getHeaders().get(headerName));
    }

    /**
     * 解析json
     *
     * @param jsonText json string
     * @param jsonPath json path
     * @return 提取结果
     */
    public static String parseJson(String jsonText, String jsonPath) throws ParseException {
        try {
            return JSON.toJSONString(JsonPath.read(jsonText, jsonPath));
        } catch (Exception e) {
            LOG.error("jsonPath解析异常，jsonText={}，jsonPath={}", jsonText, jsonPath);
            throw new ParseException("jsonPath解析异常，jsonPath: " + jsonPath);
        }
    }
}
