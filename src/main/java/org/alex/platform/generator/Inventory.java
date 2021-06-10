package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.enums.FieldType;
import org.alex.platform.exception.ValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Inventory {

    @Autowired
    Filter filter;

    /**
     * 为指定类型的属性，生成其可能的有效/无效值
     * @param key 字段名称
     * @param desc 字段描述
     * @param type 类型
     * @param config 配置
     * @return 有效值和无效值数组
     * @throws Exception Exception
     */
    public JSONArray genSingleField(Generator generator, String key, String desc, String type, JSONObject config) throws Exception {
        type = FieldType.getFieldType(type);
        Boolean allowNull = config.getBoolean("allowNull");

        switch (type) {
            case "string":
                Boolean allowIllegal = config.getBoolean("allowIllegal");
                Boolean allowEmpty = config.getBoolean("allowEmpty");
                Integer minLen = config.getInteger("minLen");
                Integer maxLen = config.getInteger("maxLen");
                filter.valid4String(allowEmpty, minLen, maxLen, allowNull);
                return generator.genString(key, desc, allowIllegal, allowEmpty, minLen, maxLen, allowNull);
            case "number":
                BigDecimal min = config.getBigDecimal("min");
                BigDecimal max = config.getBigDecimal("max");
                filter.valid4Number(min, max, allowNull);
                return generator.genNumber(key, desc, min, max, allowNull);
            case "inDb":
            case "notInDb": {
                Integer dbId = config.getInteger("dbId");
                String sql = config.getString("sql");
                String elementType = config.getString("elementType");
                filter.valid4DbData(dbId, sql, elementType, allowNull);
                if (type.equals("inDb")) {
                    return generator.genInDb(key, desc, dbId, sql, elementType, allowNull);
                } else {
                    return generator.genNotInDb(key, desc, dbId, sql, elementType, allowNull);
                }
            }
            case "const":
                Object value = config.get("value");
                filter.valid4Const(value, allowNull);
                return generator.genConst(key, desc, value, allowNull);
            case "inArray":
            case "notInArray": {
                String elementType = config.getString("elementType");
                JSONArray arrayValue = config.getJSONArray("value");
                filter.valid4ArrayData(elementType, arrayValue, allowNull);
                if (type.equals("inArray")) {
                    return generator.genInArray(key, desc, arrayValue, elementType, allowNull);
                } else {
                    return generator.genNotInArray(key, desc, arrayValue, elementType, allowNull);
                }
            }
            default:
                throw new ValidException("unknown type : " + type);
        }
    }
}
