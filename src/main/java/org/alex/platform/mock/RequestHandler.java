package org.alex.platform.mock;

import org.mockserver.model.HttpRequest;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.Header.schemaHeader;
import static org.mockserver.model.JsonPathBody.jsonPath;
import static org.mockserver.model.JsonSchemaBody.jsonSchema;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.Parameter.schemaParam;
import static org.mockserver.model.StringBody.subString;
import static org.mockserver.model.XPathBody.xpath;

public class RequestHandler {
    HttpRequest request;

    public RequestHandler(HttpRequest request) {
        this.request = request;
    }

    /** 固定值
     * 同时存在 该请求头名称以及该请求头值
     * @param name 请求头名称
     * @param value 请求头值
     */
    public void setHeader(String name, String value) {
        this.request.withHeader(name, value);
    }

    /** 名称+值正则
     * 请求头名称和值符合JsonSchema
     * @param name 请求头名称
     * @param value 请求头值
     */
    public void setHeaderByRegex(String name, String value) {
        this.request.withHeader(header(name, value));
    }

    /** 值JsonSchema
     * 请求头值符合JsonSchema
     * @param name 请求头名称
     * @param value 请求头值
     */
    public void setHeaderByJsonSchema(String name, String value) {
        this.request.withHeaders(schemaHeader(name, value));
    }



    /** 固定值
     * 同时存在 该path param名称以及该path param值
     * @param name param名称
     * @param value param值
     */
    public void setPathParam(String name, String value) {
        this.request.withPathParameter(name, value);
    }

    /** 值正则
     * 请求path param值符合正则
     * @param name param名称
     * @param value param值
     */
    public void setPathParamByRegex(String name, String value) {
        this.request.withPathParameter(param(name, value));
    }

    /** 值JsonSchema
     * 请求path param值符合JsonSchema
     * @param name param名称
     * @param value param值
     */
    public void setPathParamByJsonSchema(String name, String value) {
        this.request.withPathParameter(schemaParam(name, value));
    }



    /** 固定值
     * 同时存在 该query param名称以及该query param值
     * @param name param名称
     * @param value param值
     */
    public void setQueryParam(String name, String value) {
        this.request.withQueryStringParameter(name, value);
    }

    /** 值正则
     * 请求query param值符合正则
     * @param name param名称
     * @param value param值
     */
    public void setQueryParamByRegex(String name, String value) {
        this.request.withQueryStringParameter(param(name, value));
    }

    /** 值JsonSchema
     * 请求query param值符合JsonSchema
     * @param name param名称
     * @param value param值
     */
    public void setQueryParamByJsonSchema(String name, String value) {
        this.request.withQueryStringParameter(schemaParam(name, value));
    }


    /** 固定值
     * 请求body为该字符串
     * @param body body
     */
    public void setBody(String body) {
        this.request.withBody(body);
    }

    /** 包含
     * 请求body包含该字符串
     * @param subBody 包含内容
     */
    public void setBodyByContains(String subBody) {
        this.request.withBody(subString(subBody));
    }

    /** xpath
     * 请求body匹配xpath
     * @param xpath xpath
     */
    public void setBodyByXpath(String xpath) {
        this.request.withBody(xpath(xpath));
    }

    /** jsonPath
     * 请求body匹配jsonPath
     * @param jsonPath jsonPath
     */
    public void setBodyByJsonPath(String jsonPath) {
        this.request.withBody(jsonPath(jsonPath));
    }

    /** json schema
     * 请求body匹配json schema
     * @param jsonSchema jsonSchema
     */
    public void setBodyByJsonSchema(String jsonSchema) {
        this.request.withBody(jsonSchema(jsonSchema));
    }
}
