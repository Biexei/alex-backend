package org.alex.platform.enums;

public enum AssertType {

    JSON("json", (byte)0),
    HTML("html", (byte)1),
    HEADER("header", (byte)2),
    RESPONSE_CODE("responseCode", (byte)3),
    RUNTIME("runtime", (byte)4),

    //兼容模式
    JSON_("jsonPath", (byte)0),
    JSON__("json_path", (byte)0),
    HTML_("xpath", (byte)1),
    HEADER_("head", (byte)2),
    HEADER__("headers", (byte)2),
    RESPONSE_CODE_("code", (byte)3),
    RESPONSE_CODE__("response_code", (byte)3),
    RUNTIME_("time", (byte)4),;

    private final String assertTypeName;
    private final Byte assertTypeNum;

    public static Byte getAssertTypeKey(String assertTypeName) {
        for(AssertType assertType : AssertType.values()) {
            if (assertType.assertTypeName.equalsIgnoreCase(assertTypeName)) {
                return assertType.assertTypeNum;
            }
        }
        return 0;
    }

    AssertType(String assertTypeName, Byte assertTypeNum) {
        this.assertTypeName = assertTypeName;
        this.assertTypeNum = assertTypeNum;
    }
}
