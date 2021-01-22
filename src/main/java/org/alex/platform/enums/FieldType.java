package org.alex.platform.enums;

import org.alex.platform.exception.ValidException;

public enum FieldType {
    STRING("string","string"),
    STRING_("str","string"),

    NUMBER("number","number"),
    NUMBER_("num","number"),

    BOOLEAN("bool","boolean"),
    BOOLEAN_("boolean","boolean"),

    INTEGER("integer","integer"),
    INTEGER_("int","integer"),

    FLOAT_("float","float"),

    IN_DB("inDb","inDb"),
    NOT_IN_DB("notInDb","notInDb"),

    CONST("const","const"),
    ;

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
