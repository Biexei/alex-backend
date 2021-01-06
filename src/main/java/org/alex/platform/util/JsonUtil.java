package org.alex.platform.util;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.ParseException;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"rawtypes"})
public class JsonUtil {
    public static List jsonString2List(String jsonString) throws ParseException {
        List var1;
        try {
            var1 = JSONObject.parseObject(jsonString, List.class);
        } catch (Exception e) {
            throw new ParseException("Json parse to List exception: " + e.getMessage());
        }
        return var1;
    }

    public static JSONObject jsonString2Object(String jsonString) throws ParseException {
        JSONObject var1;
        try {
            var1 = JSONObject.parseObject(jsonString);
        } catch (Exception e) {
            throw new ParseException("Json parse to JSONObject exception: " + e.getMessage());
        }
        return var1;
    }

    public static HashMap jsonString2HashMap(String jsonString) throws ParseException {
        HashMap var1;
        try {
            var1 = JSONObject.parseObject(jsonString, HashMap.class);
        } catch (Exception e) {
            throw new ParseException("Json parse to Map exception: " + e.getMessage());
        }
        return var1;
    }

    public static Object getJsonArrayIndex(String jsonString, int index) throws ParseException {
        if (index < 0) {
            throw new ParseException("index illegal: Index: " + index);
        }
        List var1 = jsonString2List(jsonString);
        if (index >= var1.size()) {
            throw new ParseException("index out of bounds: Index: " + index + ", Size: " + var1.size());
        }
        return var1.get(index);
    }

    public static Object getJsonArrayIndex(String jsonString, int index, String defaultValue) throws ParseException {
        if (defaultValue == null) {
            throw new ParseException("default value should not be null");
        }
        if (index < 0) {
            return defaultValue;
        }
        List var1 = jsonString2List(jsonString);
        if (index >= var1.size()) {
            return defaultValue;
        }
        return var1.get(index);
    }
}
