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
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@SuppressWarnings({"unchecked","rawtypes"})
public class RestUtil {
    @Autowired
    HttpSettingMapper httpSettingMapper;

    private static final String[] DEFAULT_USER_AGENT = {"Alex"};
    private static final String[] DEFAULT_ACCEPT = {"*/*"};
    private static final String[] DEFAULT_ACCEPT_ENCODING = {"gzip, deflate, br"};
    private static final String[] DEFAULT_ACCEPT_LANGUAGE = {"zh-CN,zh;q=0.9"};
    private static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    private static final int DEFAULT_READ_TIMEOUT = 30 * 1000;
    private static RestUtil restUtil;

    private static class SingleRestTemplate {
        private static final RestTemplate INSTANCE = new RestTemplate();
    }

    public static RestTemplate getInstance() {
        RestTemplate restTemplate = SingleRestTemplate.INSTANCE;
        restTemplate.setRequestFactory(requestFactory());
        restTemplate.setErrorHandler(errorHandler());
        return restTemplate;
    }

    /**
     * 配置请求设置
     * @return 请求设置
     */
    private static SimpleClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory sh = new SimpleClientHttpRequestFactory();
        HttpSettingVO proxy = getProxy();
        if (proxy != null) {
            String[] domain = proxy.getValue().split(":");
            String host = domain[0];
            int port = Integer.parseInt(domain[1]);
            sh.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
        }
        Integer connectTimeout = getTimeout(true);
        Integer readTimeout = getTimeout(false);
        if (connectTimeout == null) {
            connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        } else if (connectTimeout < 0) {
            connectTimeout = -1;
        }
        if (readTimeout == null) {
            readTimeout = DEFAULT_READ_TIMEOUT;
        } else if (readTimeout < 0) {
            readTimeout = -1;
        }
        sh.setConnectTimeout(connectTimeout);
        sh.setReadTimeout(readTimeout);
        return sh;
    }

    /**
     * 错误处理
     * @return 错误处理
     */
    private static ResponseErrorHandler errorHandler() {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) {

            }
        };
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

        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = getDefaultHeader();

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
     * patch
     * @param url url
     * @param headers 求头
     * @param params 请求参数
     * @param data 请求data
     * @param json 请求json
     * @return 响应实体
     * @throws BusinessException 业务异常
     */
    public static ResponseEntity patch(String url, HashMap<String, String> headers, HashMap<String, String> params,
                                     HashMap<String, String> data, String json) throws BusinessException {
        return notGetRequest(HttpMethod.PATCH, url, headers, params, data, json);
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

        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = getDefaultHeader();

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

    /**
     * 获取超时时常单位秒，0ConnectTimeout  1ReadTimeout
     * @return 秒
     */
    public static Integer getTimeout(boolean isConnectTimeout) {
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        if (isConnectTimeout) {
            httpSettingDTO.setType((byte) 3);
        } else {
            httpSettingDTO.setType((byte) 4);
        }
        List<HttpSettingVO> list = restUtil.httpSettingMapper.selectHttpSetting(httpSettingDTO);
        if (!list.isEmpty()) {
            String value = list.get(0).getValue();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private static HttpHeaders getDefaultHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("User-Agent", Arrays.asList(DEFAULT_USER_AGENT));
        httpHeaders.put("Accept", Arrays.asList(DEFAULT_ACCEPT));
        httpHeaders.put("Accept-Encoding", Arrays.asList(DEFAULT_ACCEPT_ENCODING));
        httpHeaders.put("Accept-Language", Arrays.asList(DEFAULT_ACCEPT_LANGUAGE));
        return httpHeaders;
    }
}
