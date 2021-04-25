package org.alex.platform.mock;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.core.parser.Parser;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.MockApiService;
import org.alex.platform.service.MockHitPolicyService;
import org.alex.platform.service.MockServerService;
import org.alex.platform.util.NoUtil;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("rawtypes")
public class InjectionCenter {
    @Autowired
    MockServerService mockServerService;
    @Autowired
    MockApiService mockApiService;
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    MockHitPolicyService mockHitPolicyService;
    @Autowired
    Parser parser;


    /**
     * 删除api
     * @param apiId apiId
     */
    public void delete(Integer apiId) {
        MockApiInfoVO mockApiInfoVO = mockApiService.findMockApiById(apiId);
        Integer port = mockApiInfoVO.getPort();
        if (MockServerPool.isRunning(port)) {
            String beforeUpdateUrl = mockApiInfoVO.getUrl();
            Integer beforeUpdatePort = mockApiInfoVO.getPort();
            MockServerPool.clearByPath(beforeUpdatePort, beforeUpdateUrl);
        }
    }


    /**
     * 根据MockApiId向mock server 注入 api
     * @param apiId apiId
     * @throws BusinessException BusinessException
     * @throws ParseException ParseException
     * @throws SqlException SqlException
     */
    public void injectById(Integer apiId) throws BusinessException, ParseException, SqlException {
        MockApiInfoVO mockApiInfoVO = mockApiService.findMockApiById(apiId);
        List<MockHitPolicyVO> policies = mockHitPolicyService.findMockHitPolicyByApiId(apiId);

        String url = mockApiInfoVO.getUrl();
        String method = mockApiInfoVO.getMethod();
        Integer responseCode = mockApiInfoVO.getResponseCode();
        String responseHeaders = mockApiInfoVO.getResponseHeaders();
        String responseBody = mockApiInfoVO.getResponseBody();
        Byte responseBodyType = mockApiInfoVO.getResponseBodyType();
        Integer responseDelay = mockApiInfoVO.getResponseDelay();
        Byte responseBodyEnableRely = mockApiInfoVO.getResponseBodyEnableRely();
        Byte responseHeadersEnableRely = mockApiInfoVO.getResponseHeadersEnableRely();
        Integer port = mockApiInfoVO.getPort();

        this.inject(port, url, method, responseCode, responseHeaders, responseBody, responseBodyType, responseDelay,
                responseBodyEnableRely, responseHeadersEnableRely, policies);

    }
    /**
     * 根据MockApiAndPolicyDO向mock server 注入 api
     * @param policyDO policyDO
     * @throws BusinessException BusinessException
     * @throws ParseException ParseException
     * @throws SqlException SqlException
     */
    public void injectByDO(MockApiAndPolicyDO policyDO) throws BusinessException, ParseException, SqlException {
        Integer serverId = policyDO.getServerId();
        String url = policyDO.getUrl();
        String method = policyDO.getMethod();
        Integer responseCode = policyDO.getResponseCode();
        String responseHeaders = policyDO.getResponseHeaders();
        String responseBody = policyDO.getResponseBody();
        Byte responseBodyType = policyDO.getResponseBodyType();
        Integer responseDelay = policyDO.getResponseDelay();
        Byte responseBodyEnableRely = policyDO.getResponseBodyEnableRely();
        Byte responseHeadersEnableRely = policyDO.getResponseHeadersEnableRely();
        MockServerVO mockServerVO = mockServerService.findMockServerById(serverId);
        Integer port = mockServerVO.getPort();
        List<MockHitPolicyDO> policies = policyDO.getPolicies();

        this.inject(port, url, method, responseCode, responseHeaders, responseBody, responseBodyType, responseDelay,
                responseBodyEnableRely, responseHeadersEnableRely, policies);

    }

    private void inject(Integer port, String url, String method, Integer responseCode, String responseHeaders,
                        String responseBody, Byte responseBodyType, Integer responseDelay,
                        Byte responseBodyEnableRely, Byte responseHeadersEnableRely, List<? extends MockHitPolicyDO> policies) throws BusinessException, ParseException, SqlException {
        if (MockServerPool.isRunning(port)) { //只有mock server启动时才注入 api
            ClientAndServer server = MockServerPool.justGet(port); //无需关注是否启用代理，justGet仅获取，而不是检测未运行强制启用
            HttpRequest request = this.injectRequest(new HttpRequest(), method, url, policies);
            HttpResponse response = this.injectResponse(new HttpResponse(), responseCode, responseHeaders, responseBody,
                    responseBodyType, responseDelay, responseBodyEnableRely, responseHeadersEnableRely);
            server.when(request).respond(response);
        }
    }

    public HttpRequest injectRequest(HttpRequest request, String method, String url, List<? extends MockHitPolicyDO> policies) throws BusinessException {
        RequestHandler requestHandler = new RequestHandler(request);
        // 添加请求路径 请求方式策略
        requestHandler.setMethod(method);
        requestHandler.setUrl(url);
        // 添加请求头、QueryParam、PathParam、Body命中策略
        for (MockHitPolicyDO policy : policies) {
            if (policy.getStatus() == 0) { //仅启用添加命中策略
                Byte matchScope = policy.getMatchScope(); //0请求头1请求body2path param3请求query param
                Byte matchType = policy.getMatchType(); //0固定值1包含2正则3json schema4xpath5json path
                String name = policy.getName();
                String value = policy.getValue();
                if (matchScope == 0) {
                    if (matchType == 0) {
                        requestHandler.setHeader(name, value);
                    } else if (matchType == 1) {
                        requestHandler.setHeaderByRegex(name, value);
                    } else if (matchType == 2) {
                        requestHandler.setHeaderByRegex(name, value);
                    } else if (matchType == 3) {
                        requestHandler.setHeaderByJsonSchema(name, value);
                    } else if (matchType == 4) {
                        requestHandler.setHeaderByRegex(name, value);
                    } else {
                        requestHandler.setHeaderByRegex(name, value);
                    }
                } else if (matchScope == 1) {
                    if (matchType == 0) {
                        requestHandler.setBody(value);
                    } else if (matchType == 1) {
                        requestHandler.setBodyByContains(value);
                    } else if (matchType == 2) {
                        requestHandler.setBodyRegex(value);
                    } else if (matchType == 3) {
                        requestHandler.setBodyByJsonSchema(value);
                    } else if (matchType == 4) {
                        requestHandler.setBodyByXpath(value);
                    } else {
                        requestHandler.setBodyByJsonPath(value);
                    }
                } else if (matchScope == 2) {
                    if (matchType == 0) {
                        requestHandler.setPathParam(name, value);
                    } else if (matchType == 1) {
                        requestHandler.setPathParamByRegex(name, value);
                    } else if (matchType == 2) {
                        requestHandler.setPathParamByRegex(name, value);
                    } else if (matchType == 3) {
                        requestHandler.setPathParamByJsonSchema(name, value);
                    } else if (matchType == 4) {
                        requestHandler.setPathParamByRegex(name, value);
                    } else {
                        requestHandler.setPathParamByRegex(name, value);
                    }
                } else {
                    if (matchType == 0) {
                        requestHandler.setQueryParam(name, value);
                    } else if (matchType == 1) {
                        requestHandler.setQueryParamByRegex(name, value);
                    } else if (matchType == 2) {
                        requestHandler.setQueryParamByRegex(name, value);
                    } else if (matchType == 3) {
                        requestHandler.setQueryParamByJsonSchema(name, value);
                    } else if (matchType == 4) {
                        requestHandler.setQueryParamByRegex(name, value);
                    } else {
                        requestHandler.setQueryParamByRegex(name, value);
                    }
                }
            }
        }
        return request;
    }

    public HttpResponse injectResponse(HttpResponse response, Integer responseCode, String responseHeaders,
                                       String responseBody, Byte responseBodyType, Integer responseDelay,
                                       Byte responseBodyEnableRely, Byte responseHeadersEnableRely) throws BusinessException, SqlException, ParseException {
        ResponseHandler responseHandler = new ResponseHandler(response);
        responseHandler.setHttpCode(responseCode); //设置响应状态码
        if (responseDelay != null && responseDelay > 0) { //设置响应延迟
            responseHandler.setDelay(responseDelay);
        }
        // 设置响应头
        String headers = responseHeaders;
        if (responseHeadersEnableRely == 0 && headers != null) { // 解析后再加入
            headers = parser.parseDependency(headers, NoUtil.genChainNo(), null, (byte)0,
                    null, null, null, null, NoUtil.genCasePreNo());
        }
        JSONObject object;
        try {
            object = JSONObject.parseObject(headers);
        } catch (Exception e) {
            throw new BusinessException("response header must be json object string");
        }
        if (object != null) {
            for (Map.Entry entry: object.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                responseHandler.setHeader(key.toString(), value == null ? "" : value.toString());
            }
        }
        // 设置响应正文
        String body = responseBody;
        if (responseBodyEnableRely == 0 && body != null) { // 解析后再加入
            body = parser.parseDependency(body, NoUtil.genChainNo(), null, (byte)0,
                    null, null, null, null, NoUtil.genCasePreNo());
        }
        if (responseBodyType == 0) { //0文本1json2xml3html
            responseHandler.setBody(body);
        } else if (responseBodyType == 1) {
            responseHandler.setJsonBody(body);
        } else if (responseBodyType == 2) {
            responseHandler.setXmlBody(body);
        } else {
            responseHandler.setHtmlBody(body);
        }
        return response;
    }
}
