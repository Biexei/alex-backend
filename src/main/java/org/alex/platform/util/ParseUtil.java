package org.alex.platform.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
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
            List<String> result = new ArrayList<>();
            for (JXNode node : nodes) {
                result.add(node.toString());
            }
            return JSON.toJSONString(result);
        } catch (Exception e) {
            String msg = String.format("xpath parse error! xml/html=[%s], xpath=[%s], error-msg=[%s]",
                    xmlText, xpath, e.getMessage());
            LOG.error(msg);
            throw new ParseException(msg);
        }
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
            String msg = String.format("header name not found! header-name=[%s]", headerName);
            LOG.error(msg);
            throw new ParseException(msg);
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
            Configuration config = Configuration.builder().options(Option.ALWAYS_RETURN_LIST).build();
            return JSON.toJSONString(JsonPath.using(config).parse(jsonText).read(jsonPath));
        } catch (Exception e) {
            String msg = String.format("json parse error! json=[%s], json-path=[%s], error-msg=[%s]",
                    jsonText, jsonPath, e.getMessage());
            LOG.error(msg);
            throw new ParseException(msg);
        }
    }
}
