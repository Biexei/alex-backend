package org.alex.platform.mock;

import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;

import java.nio.charset.StandardCharsets;

public class ResponseHandler {
    HttpResponse response;

    public ResponseHandler(HttpResponse response) {
        this.response = response;
    }

    public void setHttpCode(Integer code) {
        this.response.withStatusCode(code);
    }

    public void setHeader(String name, String value) {
        this.response.withHeader(name, value);
    }

    public void setBody(String body) {
        this.response.withBody(body, StandardCharsets.UTF_8);
    }

    public void setJsonBody(String body) {
        this.response.withBody(body, MediaType.APPLICATION_JSON_UTF_8);
    }

    public void setXmlBody(String body) {
        this.response.withBody(body, MediaType.APPLICATION_XML_UTF_8);
    }

    public void setHtmlBody(String body) {
        this.response.withBody(body, MediaType.APPLICATION_XHTML_XML);
    }
}
