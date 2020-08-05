package org.alex.platform.util;

import com.alibaba.fastjson.JSON;
import org.alex.platform.exception.BusinessException;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class RestUtil {

    private static class SingleRestTemplate {
        private static final RestTemplate INSTANCE = new RestTemplate();
    }

    private static RestTemplate getInstance() {
        RestTemplate restTemplate = SingleRestTemplate.INSTANCE;
        // 下三行开启代理
        //SimpleClientHttpRequestFactory sh = new SimpleClientHttpRequestFactory();
        //sh.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)));
        //restTemplate.setRequestFactory(sh);
        return restTemplate;
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
    public static ResponseEntity postData(String url, HashMap<String, String> headers, HashMap<String, String> data)
            throws BusinessException {
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
     * 同时支持params和data的post
     *
     * @param url
     * @param headers
     * @param params
     * @param data
     * @return
     * @throws BusinessException
     */
    public static ResponseEntity post(String url, HashMap<String, String> headers, HashMap<String,
            String> params, HashMap<String, String> data) throws BusinessException {
        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();
        if (data == null) {
            throw new BusinessException("data不能为空");
        }
        if (params == null) {
            throw new BusinessException("params不能为空");
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
        MultiValueMap paramsMap = new LinkedMultiValueMap<String, String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsMap.add(entry.getKey(), entry.getValue());
        }
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramsMap);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, new HttpEntity(formData, httpHeaders), String.class);
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

    /**
     * 获取http响应头
     *
     * @param response
     * @return
     */
    public static Map<String, String> header(ResponseEntity response) {
        return response.getHeaders().toSingleValueMap();
    }

}
