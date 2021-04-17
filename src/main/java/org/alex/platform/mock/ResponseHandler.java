package org.alex.platform.mock;

import org.alex.platform.exception.BusinessException;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ResponseHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseHandler.class);
    HttpResponse response;

    protected ResponseHandler(HttpResponse response) {
        this.response = response;
    }

    protected void setDelay(Integer ms) throws BusinessException {
        try {
            this.response.withDelay(TimeUnit.MILLISECONDS, ms);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查响应延时");
        }
    }

    protected void setHttpCode(Integer code) throws BusinessException {
        try {
            this.response.withStatusCode(code);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查响应状态码");
        }
    }

    protected void setHeader(String name, String value) throws BusinessException {
        try {
            this.response.withHeader(name, value);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查响应头");
        }
    }

    protected void setBody(String body) throws BusinessException {
        try {
            this.response.withBody(body, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查响应体");
        }
    }

    protected void setJsonBody(String body) throws BusinessException {
        try {
            this.response.withBody(body, MediaType.APPLICATION_JSON_UTF_8);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查响应体");
        }
    }

    protected void setXmlBody(String body) throws BusinessException {
        try {
            this.response.withBody(body, MediaType.APPLICATION_XML_UTF_8);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查响应体");
        }
    }

    protected void setHtmlBody(String body) throws BusinessException {
        try {
            this.response.withBody(body, MediaType.APPLICATION_XHTML_XML);
        } catch (Exception e) {
            LOG.error("{}", e.getMessage(), e);
            throw new BusinessException("请检查响应体");
        }
    }
}
