package org.alex.platform.enums;

public enum AssertType {

    JSON("json", 0),
    HTML("html", 1),
    HEADER("header", 2),
    RESPONSE_CODE("responseCode", 3);

    private final String assertTypeName;
    private final Integer assertTypeNum;

    AssertType(String assertTypeName, Integer assertTypeNum) {
        this.assertTypeName = assertTypeName;
        this.assertTypeNum = assertTypeNum;
    }
}
