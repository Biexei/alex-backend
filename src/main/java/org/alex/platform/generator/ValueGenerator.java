package org.alex.platform.generator;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.enums.FieldType;
import org.alex.platform.exception.ValidException;

public class ValueGenerator {

    public void genString(String key, String desc, String type, JSONObject publicConfig, JSONObject privateConfig) throws ValidException {
        type = FieldType.getFieldType(type);

        boolean allowNull = publicConfig.getBoolean("allowNull");
        boolean allowRepeat = publicConfig.getBoolean("allowRepeat");
        int validEquivalenceClassSize = publicConfig.getInteger("validEquivalenceClassSize");
        int invalidEquivalenceClassSize = publicConfig.getInteger("invalidEquivalenceClassSize");


        if (type.equals("string")) {
            boolean allowIllegal = privateConfig.getBooleanValue("allowIllegal");
            int minLen = privateConfig.getIntValue("minLen");
            int maxLen = privateConfig.getIntValue("maxLen");
        } else if (type.equals("number")) {
            int minIntLen = privateConfig.getIntValue("minIntLen");
            int maxIntLen = privateConfig.getIntValue("maxIntLen");
            int minDecLen = privateConfig.getIntValue("minDecLen");
            int maxDecLen = privateConfig.getIntValue("maxDecLen");
        } else if (type.equals("boolean")) {

        } else if (type.equals("integer")) {

        } else if (type.equals("float")) {

        } else if (type.equals("InDb")) {

        } else if (type.equals("NotInDb")) {

        } else if (type.equals("const")) {

        } else {
            throw new ValidException("unknown type : " + type);
        }
    }
}
