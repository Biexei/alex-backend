package org.alex.platform.enums;

/**
 * 请求方式枚举
 */
public enum CaseMethod {

    GET("get", 0),
    POST("post", 1),
    PATCH("patch", 2),
    PUT("put", 3),
    DELETE("delete", 4);

    private final String methodName;
    private final Integer methodNum;

    CaseMethod(String methodName, Integer methodNum) {
        this.methodName = methodName;
        this.methodNum = methodNum;
    }
}
