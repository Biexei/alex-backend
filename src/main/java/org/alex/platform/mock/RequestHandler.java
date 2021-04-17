package org.alex.platform.mock;

import org.alex.platform.exception.BusinessException;
import org.mockserver.model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.Header.schemaHeader;
import static org.mockserver.model.JsonPathBody.jsonPath;
import static org.mockserver.model.JsonSchemaBody.jsonSchema;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.Parameter.schemaParam;
import static org.mockserver.model.RegexBody.regex;
import static org.mockserver.model.StringBody.subString;
import static org.mockserver.model.XPathBody.xpath;

public class RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

    HttpRequest request;

    protected RequestHandler(HttpRequest request) {
        this.request = request;
    }

    /**
     * 设置请求头 支持正则
     * @param method 请求方式
     */
    protected void setMethod(String method) throws BusinessException {
        try {
            this.request.withMethod(method);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求方式");
        }
    }

    /**
     * 设置url 支持正则
     * @param url 请求url
     */
    protected void setUrl(String url) throws BusinessException {
        try {
            this.request.withPath(url);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求地址");
        }
    }

    /** 固定值
     * 同时存在 该请求头名称以及该请求头值
     * @param name 请求头名称
     * @param value 请求头值
     */
    protected void setHeader(String name, String value) throws BusinessException {
        try {
            this.request.withHeader(name, value);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求头");
        }
    }

    /** 名称+值正则
     * 请求头名称和值符合正则
     * @param name 请求头名称
     * @param value 请求头值
     */
    protected void setHeaderByRegex(String name, String value) throws BusinessException {
        try {
            if(value == null) {
                this.request.withHeader(header(name));
            } else {
                this.request.withHeader(header(name, value));
            }
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求头");
        }
    }

    /** 值JsonSchema
     * 请求头值符合JsonSchema
     * @param name 请求头名称
     * @param value 请求头值
     */
    protected void setHeaderByJsonSchema(String name, String value) throws BusinessException {
        try {
            this.request.withHeaders(schemaHeader(name, value));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求头");
        }
    }



    /** 固定值
     * 同时存在 该path param名称以及该path param值
     * @param name param名称
     * @param value param值
     */
    protected void setPathParam(String name, String value) throws BusinessException {
        try {
            this.request.withPathParameter(name, value);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查path param");
        }
    }

    /** 值正则
     * 请求path param值符合正则
     * @param name param名称
     * @param value param值
     */
    protected void setPathParamByRegex(String name, String value) throws BusinessException {
        try {
            this.request.withPathParameter(param(name, value));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查path param");
        }
    }

    /** 值JsonSchema
     * 请求path param值符合JsonSchema
     * @param name param名称
     * @param value param值
     */
    protected void setPathParamByJsonSchema(String name, String value) throws BusinessException {
        try {
            this.request.withPathParameter(schemaParam(name, value));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查path param");
        }
    }



    /** 固定值
     * 同时存在 该query param名称以及该query param值
     * @param name param名称
     * @param value param值
     */
    protected void setQueryParam(String name, String value) throws BusinessException {
        try {
            this.request.withQueryStringParameter(name, value);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查query param");
        }
    }

    /** 值正则
     * 请求query param值符合正则
     * @param name param名称
     * @param value param值
     */
    protected void setQueryParamByRegex(String name, String value) throws BusinessException {
        try {
            this.request.withQueryStringParameter(param(name, value));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查query param");
        }
    }

    /** 值JsonSchema
     * 请求query param值符合JsonSchema
     * @param name param名称
     * @param value param值
     */
    protected void setQueryParamByJsonSchema(String name, String value) throws BusinessException {
        try {
            this.request.withQueryStringParameter(schemaParam(name, value));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查query param");
        }
    }


    /** 固定值
     * 请求body为该字符串
     * @param body body
     */
    protected void setBody(String body) throws BusinessException {
        try {
            this.request.withBody(body);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求body");
        }
    }

    /** 正则
     * 请求body匹配该正则
     * @param regex body
     */
    protected void setBodyRegex(String regex) throws BusinessException {
        try {
            this.request.withBody(regex(regex));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求body");
        }
    }

    /** 包含
     * 请求body包含该字符串
     * @param subBody 包含内容
     */
    protected void setBodyByContains(String subBody) throws BusinessException {
        try {
            this.request.withBody(subString(subBody));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求body");
        }
    }

    /** xpath
     * 请求body匹配xpath
     * @param xpath xpath
     */
    protected void setBodyByXpath(String xpath) throws BusinessException {
        try {
            this.request.withBody(xpath(xpath));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求body");
        }
    }

    /** jsonPath
     * 请求body匹配jsonPath
     * @param jsonPath jsonPath
     */
    protected void setBodyByJsonPath(String jsonPath) throws BusinessException {
        try {
            this.request.withBody(jsonPath(jsonPath));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求body");
        }
    }

    /** json schema
     * 请求body匹配json schema
     * @param jsonSchema jsonSchema
     */
    protected void setBodyByJsonSchema(String jsonSchema) throws BusinessException {
        try {
            this.request.withBody(jsonSchema(jsonSchema));
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查请求body");
        }
    }
}
