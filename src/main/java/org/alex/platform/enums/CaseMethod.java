package org.alex.platform.enums;

/**
 * 请求方式枚举
 */
public enum CaseMethod {

    GET("get", (byte)0),
    POST("post", (byte)1),
    PATCH("patch", (byte)2),
    PUT("put", (byte)3),
    DELETE("delete", (byte)4),
    HEAD("head", (byte)5),
    OPTIONS("options", (byte)6),
    TRACE("trace", (byte)7);

    private final String methodName;
    private final Byte methodNum;

    public static Byte getMethodKey(String methodName) {
        for(CaseMethod caseMethod : CaseMethod.values()) {
            if (caseMethod.methodName.equalsIgnoreCase(methodName)) {
                return caseMethod.methodNum;
            }
        }
        return 0;
    }

    CaseMethod(String methodName, Byte methodNum) {
        this.methodName = methodName;
        this.methodNum = methodNum;
    }
}
