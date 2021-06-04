package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.ValidException;

import java.math.BigDecimal;

public class DynamicGenerator implements Generator{
    @Override
    public JSONArray genSingleField(String key, String desc, String type, JSONObject config) throws Exception {
        return null;
    }

    @Override
    public JSONArray genString(String key, String desc, Boolean allowIllegal, Boolean allowEmpty, Integer minLen, Integer maxLen, Boolean allowNull) {
        return null;
    }

    @Override
    public JSONArray genNumber(String key, String desc, BigDecimal min, BigDecimal max, Boolean allowNull) {
        return null;
    }

    @Override
    public JSONArray genInDb(String key, String desc, Integer dbId, String sql, String elementType, Boolean allowNull) throws Exception {
        return null;
    }

    @Override
    public JSONArray genNotInDb(String key, String desc, Integer dbId, String sql, String elementType, Boolean allowNull) throws Exception {
        return null;
    }

    @Override
    public JSONArray genConst(String key, String desc, Object value, Boolean allowNull) {
        return null;
    }

    @Override
    public JSONArray genInArray(String key, String desc, JSONArray array, String elementType, Boolean allowNull) throws ValidException {
        return null;
    }

    @Override
    public JSONArray genNotInArray(String key, String desc, JSONArray array, String elementType, Boolean allowNull) throws ValidException {
        return null;
    }
}
