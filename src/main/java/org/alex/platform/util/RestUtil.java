package org.alex.platform.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.HttpSettingMapper;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.pojo.HttpSettingVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RestUtil {
    @Autowired
    HttpSettingMapper httpSettingMapper;
    private static RestUtil restUtil;

    private static class SingleRestTemplate {
        private static final RestTemplate INSTANCE = new RestTemplate();
    }

    public static RestTemplate getInstance() {
        RestTemplate restTemplate = SingleRestTemplate.INSTANCE;
        SimpleClientHttpRequestFactory sh = new SimpleClientHttpRequestFactory();
        // 1.设置代理
        List<HttpSettingVO> proxy = getProxy();
        if (!proxy.isEmpty()) {
            for (HttpSettingVO httpSettingVO : proxy) {
                String[] domain = httpSettingVO.getValue().split(":");
                String host = domain[0];
                int port = Integer.parseInt(domain[1]);
                sh.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
            }
        }
        // 2.设置超时时长
        sh.setConnectTimeout(10 * 1000);
        sh.setReadTimeout(10 * 1000);
        restTemplate.setRequestFactory(sh);
        // 3.去除code != 200时异常信息
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

            }
        });
        // 4.配置全局headers
        // 2020-10-12去除，造成并行执行case时java.util.ConcurrentModificationException
//        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
//            @Override
//            public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution
//                    clientHttpRequestExecution) throws IOException {
//                HttpHeaders headers = httpRequest.getHeaders();
//                // 1.设置请求头
//                List<HttpSettingVO> header = getHeader();
//                if (!header.isEmpty()) {
//                    for (HttpSettingVO httpSettingVO : header) {
//                        LinkedList<String> list = new LinkedList<>();
//                        list.add(httpSettingVO.getValue());
//                        headers.put(httpSettingVO.getName(), list);
//                    }
//                }
//                return clientHttpRequestExecution.execute(httpRequest, bytes);
//            }
//        });
        return restTemplate;
    }

    /**
     * get请求,支持动态url,如http://www.baidu.com/name/{name}/{class}
     * @param url 请求地址，不允许为空
     * @param headers 请求头，可为空
     * @param params 请求参数，可为空
     * @return 响应实体
     * @throws BusinessException 业务异常
     */
    public static ResponseEntity get(String url, HashMap<String, String> headers, HashMap<String, String> params)
            throws BusinessException {

        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();

        // 0.检查url
        if (StringUtils.isEmpty(url)) {
            throw new BusinessException("url不能为空");
        }
        // 1.判断是否存在头
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        // 2.判断是否有参数
        if (params == null) { // 若无参数
            return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), String.class);
        } else { // 存在参数
            // 判断url是否为restful风格，如http://www.baidu.com/name/{name}/{class}
            Pattern p = Pattern.compile("\\{(\\w)+\\}");
            Matcher m = p.matcher(url);

            if (m.groupCount() == 0) { // 如果未匹配到，则直接通过params方式发送get请求
                MultiValueMap paramsMap = new LinkedMultiValueMap<String, String>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramsMap.add(entry.getKey(), entry.getValue());
                }
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramsMap);
                return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(httpHeaders),
                        String.class);
            } else { // 匹配到rest风格
                String urlAfter = url;
                while (m.find()) {
                    String pathVariable = m.group();
                    // 判断params中是否存在pathVariable
                    if (!params.containsKey(pathVariable.substring(1, pathVariable.length() - 1))) {
                        throw new BusinessException("params未找到该pathVariable");
                    } else {
                        // 将params字典移除匹配到的字段，并把url替换
                        urlAfter = urlAfter.replace(pathVariable, params.remove(pathVariable.substring(1,
                                pathVariable.length() - 1)));
                    }
                }
                MultiValueMap paramsMap = new LinkedMultiValueMap<String, String>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramsMap.add(entry.getKey(), entry.getValue());
                }
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlAfter).queryParams(paramsMap);
                return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(httpHeaders),
                        String.class);
            }
        }
    }

    /**
     * post请求,支持动态url,如http://www.baidu.com/name/{name}/{class}
     * @param url 请求地址，不能为空
     * @param headers 请求头，可为空
     * @param params 请求参数，可为空
     * @param data 请求data，可为空，不能和json字段同时为空或同时不为空
     * @param json 请求json，可为空，不能和data字段同时为空或同时不为空
     * @return 响应实体
     * @throws BusinessException 业务异常
     */
    public static ResponseEntity post(String url, HashMap<String, String> headers, HashMap<String, String> params,
                               HashMap<String, String> data, String json) throws BusinessException {

        RestTemplate restTemplate = RestUtil.getInstance();
        HttpHeaders httpHeaders = new HttpHeaders();

        // 0.检查url
        if (StringUtils.isEmpty(url)) {
            throw new BusinessException("url不能为空");
        }
        // 1.判断是否存在头
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        // 2.data和json不能同时存在、不能同时不存在
//        if (StringUtils.isEmpty(json) && data == null) {
//            throw new BusinessException("data/json只能任传其一");
//        }
        if (StringUtils.isNotEmpty(json) && data != null) {
            throw new BusinessException("data/json只能任传其一");
        }

        // 3.判断是否有参数
        if (params == null) { // 若无参数
            // 判断发送data还是json
            if (data != null) { //走data
                //此处只能使用LinkedMultiValueMap，若使用HashMap则不会对请求表单进行编码
                LinkedMultiValueMap formData = new LinkedMultiValueMap();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    formData.add(entry.getKey(), entry.getValue());
                }
                //无参数，走data
                return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(formData, httpHeaders), String.class);
            } else { //走json
                //无参数，走json
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(json, httpHeaders), String.class);
            }
        } else { // 存在参数
            // 判断url是否为restful风格，如http://www.baidu.com/name/{name}/{class}
            Pattern p = Pattern.compile("\\{(\\w)+\\}");
            Matcher m = p.matcher(url);
            if (m.groupCount() == 0) { // 如果未匹配到，则url无需替换
                MultiValueMap paramsMap = new LinkedMultiValueMap<String, String>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramsMap.add(entry.getKey(), entry.getValue());
                }
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramsMap);
                url = builder.toUriString();
                // 判断发送data还是json
                if (data != null) { //走data
                    //此处只能使用LinkedMultiValueMap，若使用HashMap则不会对请求表单进行编码
                    LinkedMultiValueMap formData = new LinkedMultiValueMap();
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        formData.add(entry.getKey(), entry.getValue());
                    }
                    //有参数，非rest，走data
                    return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(formData, httpHeaders),
                            String.class);
                } else { //走json
                    //有参数，非rest，走json
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(json, httpHeaders),
                            String.class);
                }
            } else { // 匹配到rest风格，则url需要替换
                String urlAfter = url;
                while (m.find()) {
                    String pathVariable = m.group();
                    // 判断params中是否存在pathVariable
                    if (!params.containsKey(pathVariable.substring(1, pathVariable.length() - 1))) {
                        throw new BusinessException("params未找到该pathVariable");
                    } else {
                        // 将params字典移除匹配到的字段，并把url替换
                        urlAfter = urlAfter.replace(pathVariable, params.remove(pathVariable.substring(1,
                                pathVariable.length() - 1)));
                    }
                }
                MultiValueMap paramsMap = new LinkedMultiValueMap<String, String>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramsMap.add(entry.getKey(), entry.getValue());
                }
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlAfter).queryParams(paramsMap);
                urlAfter = builder.toUriString();
                // 判断发送data还是json
                if (data != null) { //走data
                    //此处只能使用LinkedMultiValueMap，若使用HashMap则不会对请求表单进行编码
                    LinkedMultiValueMap formData = new LinkedMultiValueMap();
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        formData.add(entry.getKey(), entry.getValue());
                    }
                    //有参数，rest，走data
                    return restTemplate.exchange(urlAfter, HttpMethod.POST, new HttpEntity(formData, httpHeaders),
                            String.class);
                } else { //走json
                    //有参数，rest，走json
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    return restTemplate.exchange(urlAfter, HttpMethod.POST, new HttpEntity(json, httpHeaders),
                            String.class);
                }
            }
        }
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
        return response.getBody().toString();
    }

    /**
     * 获取http响应头
     *
     * @param response
     * @return
     */
    public static String headers(ResponseEntity response) {
        return JSON.toJSONString(response.getHeaders());
    }

    /**
     * 获取http响应头
     *
     * @param response
     * @return
     */
    public static JSONObject header(ResponseEntity response) {
        return JSON.parseObject(JSON.toJSONString(response.getHeaders()));
    }

    @PostConstruct
    public void init() {
        restUtil = this;
        restUtil.httpSettingMapper = this.httpSettingMapper;
    }

    public static List<HttpSettingVO> getProxy() {
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        httpSettingDTO.setType((byte)0);
        return restUtil.httpSettingMapper.selectHttpSetting(httpSettingDTO);
    }

    public static List<HttpSettingVO> getHeader() {
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        httpSettingDTO.setType((byte)1);
        return restUtil.httpSettingMapper.selectHttpSetting(httpSettingDTO);
    }

}
