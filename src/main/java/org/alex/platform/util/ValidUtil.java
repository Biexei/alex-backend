package org.alex.platform.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.ValidException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidUtil {

    public static void isJsonPath(String jsonPath) throws ValidException {
        notNUll(jsonPath, "json path is null");
        notEmpty(jsonPath, "json path is empty");
        if (!jsonPath.startsWith("$")) {
            throw new ValidException("json path '" + jsonPath + "' syntax error");
        }
    }

    public static void isXpath(String xpath) throws ValidException {
        notNUll(xpath, "xpath is null");
        notEmpty(xpath, "xpath is empty");
        if (!xpath.startsWith("\\")) {
            throw new ValidException("xpath '" + xpath + "' syntax error");
        }
    }

    public static void isJsonObject(String jsonString, String errorMsg) throws ValidException {
        try {
            JSONObject.parseObject(jsonString);
        } catch (Exception e) {
            if (errorMsg == null) {
                throw new ValidException("json string error! " + e.getMessage());
            } else {
                throw new ValidException(errorMsg);
            }
        }
    }

    public static void isJsonObject(String jsonString) throws ValidException {
        isJsonObject(jsonString, null);
    }

    public static void isJsonArray(String jsonString, String errorMsg) throws ValidException {
        try {
            JSONObject.parseArray(jsonString);
        } catch (Exception e) {
            if (errorMsg == null) {
                throw new ValidException("json string error! " + e.getMessage());
            } else {
                throw new ValidException(errorMsg);
            }
        }
    }

    public static void isJsonArray(String jsonString) throws ValidException {
        isJsonArray(jsonString, null);
    }

    public static void isJson(String jsonString, String errorMsg) throws ValidException {
        boolean isObject = true;
        boolean isArray = true;
        String msg = "";
        try {
            isJsonObject(jsonString);
        } catch (ValidException e) {
            isObject = false;
            msg += e.getMessage();
        }
        try {
            isJsonArray(jsonString);
        } catch (ValidException e) {
            isArray = false;
            msg += e.getMessage();
        }
        if (!isObject && !isArray) {
            if (errorMsg == null) {
                throw new ValidException(msg);
            } else {
                throw new ValidException(errorMsg);
            }
        }
    }

    public static void isJson(String jsonString) throws ValidException {
        isJson(jsonString, null);
    }

    public static void isHashMap(String jsonString) throws ValidException {
        try {
            JSONObject.parseObject(jsonString, HashMap.class);
        } catch (Exception e) {
            throw new ValidException("json string error! " + e.getMessage());
        }
    }

    public static void isArrayList(String jsonString) throws ValidException {
        try {
            JSONObject.parseObject(jsonString, ArrayList.class);
        } catch (Exception e) {
            throw new ValidException("json string error! " + e.getMessage());
        }
    }

    public static <T> void isObject(String jsonString, Class<T> clazz) throws ValidException {
        try {
            JSONObject.parseObject(jsonString, clazz);
        } catch (Exception e) {
            throw new ValidException("json string error! " + e.getMessage());
        }
    }

    public static void notNUll(Object object, String msg) throws ValidException {
        if (object == null) {
            throw new ValidException(msg);
        }
    }

    public static void notEmpty(String s, String msg) throws ValidException {
        notNUll(s, "ValidUtil.notEmpty param 'String s' is null");
        if (s.isEmpty()) {
            throw new ValidException(msg);
        }
    }

    public static <E> void notEmpty(List<E> l, String msg) throws ValidException {
        notNUll(l, "ValidUtil.notEmpty param 'List l' is null");
        if (l.isEmpty()) {
            throw new ValidException(msg);
        }
    }

    public static void notEmpty(JSONArray array, String msg) throws ValidException {
        notNUll(array, "ValidUtil.notEmpty param 'JSONArray array' is null");
        if (array.isEmpty()) {
            throw new ValidException(msg);
        }
    }

    public static <K, V> void notEmpty(Map<K, V> m, String msg) throws ValidException {
        notNUll(m, "ValidUtil.notEmpty param 'Map m' is null");
        if (m.isEmpty()) {
            throw new ValidException(msg);
        }
    }

    public static void size(byte num, int min, int max, String msg) throws ValidException {
        if (num > max || num < min) {
            throw new ValidException(msg);
        }
    }

    public static void size(byte num, int max, String msg) throws ValidException {
        size(num, 0, max, msg);
    }

    public static void size(short num, int min, int max, String msg) throws ValidException {
        if (num > max || num < min) {
            throw new ValidException(msg);
        }
    }

    public static void size(short num, int max, String msg) throws ValidException {
        size(num, 0, max, msg);
    }

    public static void size(int num, int min, int max, String msg) throws ValidException {
        if (num > max || num < min) {
            throw new ValidException(msg);
        }
    }

    public static void size(int num, int max, String msg) throws ValidException {
        size(num, 0, max, msg);
    }

    public static void size(long num, int min, int max, String msg) throws ValidException {
        if (num > max || num < min) {
            throw new ValidException(msg);
        }
    }

    public static void size(long num, int max, String msg) throws ValidException {
        size(num, 0, max, msg);
    }

    public static void length(String s, int minLength, int maxLength, String msg) throws ValidException {
        notNUll(s, "ValidUtil.length param 's' is null");
        int sLen = s.length();
        if (sLen > maxLength || sLen < minLength) {
            throw new ValidException(msg);
        }
    }

    public static void length(String s, int maxLength, String msg) throws ValidException {
        length(s, 0, maxLength, msg);
    }

    public static void isWord(String s, String errorMsg) throws ValidException {
        notNUll(s, "param is null");
        if (!s.matches("[a-zA-Z]+")) {
            if (errorMsg == null) {
                throw new ValidException("param must be English word");
            } else {
                throw new ValidException(errorMsg);
            }
        }
    }

    public static void isWord(String s) throws ValidException {
        isWord(s, null);
    }

    public static void isWordUnderline(String s, String errorMsg) throws ValidException {
        notNUll(s, "param is null");
        if (!s.matches("[a-zA-Z_]+")) {
            if (errorMsg == null) {
                throw new ValidException("param must be English word or underline");
            } else {
                throw new ValidException(errorMsg);
            }
        }
    }

    public static void isWordUnderline(String s) throws ValidException {
        isWordUnderline(s, null);
    }

    public static void isGreaterThanOrEqualsZero(Integer value, String msg) throws ValidException {
        if (value.compareTo(0) < 0) {
            throw new ValidException(msg);
        }
    }

    public static void isGreaterThanOrEqualsZero(BigDecimal value, String msg) throws ValidException {
        if (value.compareTo(new BigDecimal(0)) < 0) {
            throw new ValidException(msg);
        }
    }
}
