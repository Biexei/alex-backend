package org.alex.platform.enums;

import org.alex.platform.exception.ValidException;

/**
 * 导入case schema 字段类型枚举
 */
public enum FieldType {
    STRING("string","string"),
    STRING_("str","string"),

    NUMBER("number","number"),
    NUMBER_("num","number"),

    IN_DB("inDb","inDb"),
    NOT_IN_DB("notInDb","notInDb"),

    IN_DB_("in_db","inDb"),
    NOT_IN_DB_("not_in_db","notInDb"),

    CONST("const","const"),

    IN_ARRAY("inArray","inArray"),
    NOT_IN_ARRAY("notInArray","notInArray"),

    IN_ARRAY_("in_array","inArray"),
    NOT_IN_ARRAY_("not_in_array","notInArray");

    private String type;
    private String value;

    FieldType(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getFieldType(String type) throws ValidException {
        for(FieldType fieldType : FieldType.values()) {
            if (fieldType.type.equalsIgnoreCase(type)) {
                return fieldType.value;
            }
        }
        throw new ValidException("unknown type : " + type);
    }
}
