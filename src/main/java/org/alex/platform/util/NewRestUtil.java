package org.alex.platform.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.HttpSettingMapper;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.pojo.HttpSettingVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@SuppressWarnings({"unchecked","rawtypes"})
public class NewRestUtil {
    @Autowired
    HttpSettingMapper httpSettingMapper;
    private static NewRestUtil restUtil;

    private static class SingleRestTemplate {
        private static final RestTemplate INSTANCE = new RestTemplate();
    }

    public static RestTemplate getInstance() {
        RestTemplate restTemplate = SingleRestTemplate.INSTANCE;
        SimpleClientHttpRequestFactory sh = new SimpleClientHttpRequestFactory();
        // 1.设置代理
        HttpSettingVO proxy = getProxy();
        if (proxy != null) {
            String[] domain = proxy.getValue().split(":");
            String host = domain[0];
            int port = Integer.parseInt(domain[1]);
            sh.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
        }
        // 2.设置超时时长
        sh.setConnectTimeout(10 * 1000);
        sh.setReadTimeout(10 * 1000);
        restTemplate.setRequestFactory(sh);
        // 3.去除code != 200时异常信息
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) {

            }
        });
        return restTemplate;
    }

    /**
     * get
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @return 响应实体
     * @throws BusinessException 业务异常
     */
    public static ResponseEntity get(String url, HashMap<String, String> headers, HashMap<String, String> params)
            throws BusinessException {

        RestTemplate restTemplate = NewRestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();

        if (StringUtils.isEmpty(url)) {
            throw new BusinessException("url should not be empty or null");
        }
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        if (params == null) {
            return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), String.class);
        } else {
            HashMap<String, Object> urlParamsWrapper = urlParamsWrapper(url, params);
            url = (String) urlParamsWrapper.get("url");
            params = (HashMap<String, String>) urlParamsWrapper.get("params");
            return restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(url).queryParams(hashMap2LinkedMultiValueMap(params))
                            .toUriString(), HttpMethod.GET, new HttpEntity(httpHeaders), String.class);
        }
    }

    /**
     * post
     * @param url url
     * @param headers 求头
     * @param params 请求参数
     * @param data 请求data
     * @param json 请求json
     * @return 响应实体
     * @throws BusinessException 业务异常
     */
    public static ResponseEntity post(String url, HashMap<String, String> headers, HashMap<String, String> params,
                                      HashMap<String, String> data, String json) throws BusinessException {
        return notGetRequest(HttpMethod.POST, url, headers, params, data, json);
    }

    /**
     * put
     * @param url url
     * @param headers 求头
     * @param params 请求参数
     * @param data 请求data
     * @param json 请求json
     * @return 响应实体
     * @throws BusinessException 业务异常
     */
    public static ResponseEntity put(String url, HashMap<String, String> headers, HashMap<String, String> params,
                                      HashMap<String, String> data, String json) throws BusinessException {
        return notGetRequest(HttpMethod.PUT, url, headers, params, data, json);
    }

    /**
     * delete
     * @param url url
     * @param headers 求头
     * @param params 请求参数
     * @param data 请求data
     * @param json 请求json
     * @return 响应实体
     * @throws BusinessException 业务异常
     */
    public static ResponseEntity delete(String url, HashMap<String, String> headers, HashMap<String, String> params,
                                     HashMap<String, String> data, String json) throws BusinessException {
        return notGetRequest(HttpMethod.DELETE, url, headers, params, data, json);
    }

    /**
     * @param httpMethod 请求方式（post/put/delete）
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @param data 请求data
     * @param json 请求json
     * @return 响应实体
     * @throws BusinessException 业务异常
     */
    private static ResponseEntity notGetRequest(HttpMethod httpMethod, String url, HashMap<String, String> headers,
                                                HashMap<String, String> params, HashMap<String, String> data, String json)
            throws BusinessException {

        RestTemplate restTemplate = NewRestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();

        if (StringUtils.isEmpty(url)) {
            throw new BusinessException("url should not be empty or null");
        }
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        if (StringUtils.isNotEmpty(json) && data != null) {
            throw new BusinessException("data or json ?");
        }

        HashMap<String, Object> urlParamsWrapper = urlParamsWrapper(url, params);
        url = (String) urlParamsWrapper.get("url");
        params = (HashMap<String, String>) urlParamsWrapper.get("params");

        if (params != null && !params.isEmpty()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(hashMap2LinkedMultiValueMap(params));
            url = builder.toUriString();
        }
        if (data != null) {
            LinkedMultiValueMap<String, String> formData = hashMap2LinkedMultiValueMap(data);
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            return restTemplate.exchange(url, httpMethod, new HttpEntity(formData, httpHeaders), String.class);
        } else {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return restTemplate.exchange(url, httpMethod, new HttpEntity(json, httpHeaders), String.class);
        }
    }

    /**
     * 解析url参数
     * @param url url
     * @param params params
     * @return 返回解析后的url和params字典
     * @throws BusinessException 解析失败 未找到key
     */
    private static HashMap<String, Object> urlParamsWrapper(String url, HashMap<String, String> params) throws BusinessException {
        Pattern p = Pattern.compile("\\{(\\w)+}");
        Matcher m = p.matcher(url);
        HashMap<String, Object> result = new HashMap<>();
        while (m.find()) {
            String pathVariable = m.group();
            String key = pathVariable.substring(1, pathVariable.length() - 1);
            if (params == null || params.isEmpty()) {
                throw new BusinessException("url parse failed, params is empty or null");
            }
            if (!params.containsKey(key)) {
                throw new BusinessException("url parse failed, '" + key + "' not found");
            } else {
                url = url.replace(pathVariable, params.remove(pathVariable.substring(1, pathVariable.length() - 1)));
            }
        }
        result.put("url", url);
        result.put("params", params);
        return result;
    }

    /**
     * HashMap2LinkedMultiValueMap
     * @param hashMap hashMap
     * @return inkedMultiValueMap
     */
    private static <K, V> LinkedMultiValueMap hashMap2LinkedMultiValueMap(HashMap<K, V> hashMap) {
        LinkedMultiValueMap<K, V> result = new LinkedMultiValueMap<>();
        if (hashMap == null) {
            return null;
        }
        if (hashMap.isEmpty()) {
            return result;
        }
        for (Map.Entry<K, V> entry : hashMap.entrySet()) {
            result.add(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 获取http响应头
     *
     * @param response ResponseEntity
     * @return http响应头
     */
    public static int code(ResponseEntity response) {
        return response.getStatusCodeValue();
    }

    /**
     * 获取http响应正文
     *
     * @param response ResponseEntity
     * @return http响应正文
     */
    public static String body(ResponseEntity response) {
        if (response.getBody() != null) {
            return response.getBody().toString();
        } else {
            return "";
        }
    }

    /**
     * 获取http响应头
     *
     * @param response response
     * @return http响应头
     */
    public static String headers(ResponseEntity response) {
        return JSON.toJSONString(response.getHeaders());
    }

    /**
     * 获取http响应头(格式化)
     *
     * @param response response
     * @return http响应头
     */
    public static String headersPretty(ResponseEntity response) {
        return JSON.toJSONString(response.getHeaders(), SerializerFeature.PrettyFormat);
    }

    /**
     * 获取http响应头
     *
     * @param response ResponseEntity
     * @return http响应头
     */
    public static JSONObject header(ResponseEntity response) {
        return JSON.parseObject(JSON.toJSONString(response.getHeaders()));
    }

    @PostConstruct
    public void init() {
        restUtil = this;
        restUtil.httpSettingMapper = this.httpSettingMapper;
    }

    public static HttpSettingVO getProxy() {
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        httpSettingDTO.setType((byte)0);
        List<HttpSettingVO> list = restUtil.httpSettingMapper.selectHttpSetting(httpSettingDTO);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<HttpSettingVO> getHeader() {
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        httpSettingDTO.setType((byte)1);
        return restUtil.httpSettingMapper.selectHttpSetting(httpSettingDTO);
    }
}
