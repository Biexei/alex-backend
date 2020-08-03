package org.alex.platform.util;

import com.alibaba.fastjson.JSON;
import org.alex.platform.exception.BusinessException;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestUtil {

    private static class SingleRestTemplate {
        private static final RestTemplate INSTANCE = new RestTemplate();
    }

    private static RestTemplate getInstance() {
        return SingleRestTemplate.INSTANCE;
    }

    /**
     * 无头无参GET请求
     *
     * @param url
     * @return
     */
    public static ResponseEntity get(String url) {
        RestTemplate rt = RestUtil.getInstance();
        return rt.getForEntity(url, String.class);
    }


    /**
     * 有头有参GET请求
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static ResponseEntity get(String url, HashMap<String, String> headers, HashMap<String, String> params) {
        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(params, httpHeaders), String.class);
    }

    /**
     * formdata post
     *
     * @param url
     * @param headers
     * @param data
     * @return
     * @throws BusinessException
     */
    public static ResponseEntity postData(String url, HashMap<String, String> headers, HashMap<String, String> data) throws BusinessException {
        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();
        if (data == null) {
            throw new BusinessException("data不能为空");
        }
        //此处只能使用LinkedMultiValueMap，若使用HashMap则不会对请求表单进行编码
        LinkedMultiValueMap formData = new LinkedMultiValueMap();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            formData.add(entry.getKey(), entry.getValue());
        }
        //判断是否指定请求头
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(formData, httpHeaders), String.class);
    }

    /**
     * json字符串 post
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static ResponseEntity postJson(String url, HashMap<String, String> headers, String json) {
        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(json, httpHeaders), String.class);
    }

    /**
     * json post
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static ResponseEntity postJson(String url, HashMap<String, String> headers, HashMap<String, Object> json) {
        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(json, httpHeaders), String.class);
    }


    /**
     * 获取http响应头
     *
     * @param response
     * @return
     */
    public static int code(ResponseEntity response) {
        return response.getStatusCodeValue();
    }

    /**
     * 获取http响应正文
     *
     * @param response
     * @return
     */
    public static String body(ResponseEntity response) {
        return JSON.toJSONString(response.getBody());
    }

    /**
     * 获取http响应头
     *
     * @param response
     * @return
     */
    public static String headers(ResponseEntity response) {
        return JSON.toJSONString(response.getHeaders().toSingleValueMap());
    }

}
