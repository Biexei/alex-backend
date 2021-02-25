package org.alex.platform.enums;

import org.alex.platform.exception.ValidException;

/**
 * 返回值字段类型枚举
 */
public enum ResultType {
    STRING("string","string"),
    STRING_("str","string"),

    NUMBER("number","number"),
    NUMBER_("num","number"),
    NUMBER__("bigDecimal","number"),

    INTEGER("integer","integer"),
    INTEGER_("int","integer"),

    FLOAT("float","float"),

    DOUBLE("double","double")
    ;

    private String type;
    private String value;

    ResultType(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getResultType(String type) throws ValidException {
        for(ResultType fieldType : ResultType.values()) {
            if (fieldType.type.equalsIgnoreCase(type)) {
                return fieldType.value;
            }
        }
        throw new ValidException("unknown type : " + type);
    }
}
