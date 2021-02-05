package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.enums.CaseType;
import org.alex.platform.enums.FieldType;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Generator {
    @Autowired
    Valid valid;
    @Autowired
    Description description;

    public JSONArray genSingleField(String key, String desc, String type, JSONObject publicConfig, JSONObject privateConfig) throws BusinessException {
        type = FieldType.getFieldType(type);

        Boolean allowNull = publicConfig.getBoolean("allowNull");
        Boolean allowRepeat = publicConfig.getBoolean("allowRepeat");
        valid.valid4GlobalConfig(allowNull, allowRepeat);

        if (type.equals("string")) {
            Boolean allowIllegal = privateConfig.getBoolean("allowIllegal");
            Boolean allowEmpty = privateConfig.getBoolean("allowEmpty");
            Integer minLen = privateConfig.getInteger("minLen");
            Integer maxLen = privateConfig.getInteger("maxLen");
            valid.valid4String(allowIllegal, allowEmpty, minLen, maxLen);
            return genString(key, desc, publicConfig, allowIllegal, allowEmpty, minLen, maxLen);
        } else if (type.equals("number")) {
            BigDecimal minimum = privateConfig.getBigDecimal("minimum");
            BigDecimal maximum = privateConfig.getBigDecimal("maximum");
            Integer minIntLen = privateConfig.getInteger("minIntLen");
            Integer maxIntLen = privateConfig.getInteger("maxIntLen");
            Integer minDecLen = privateConfig.getInteger("minDecLen");
            Integer maxDecLen = privateConfig.getInteger("maxDecLen");
            valid.valid4Number(minimum, maximum, minIntLen, maxIntLen, minDecLen, maxDecLen);
            return null;
        } else if (type.equals("integer")) {
            Integer minimum = privateConfig.getInteger("minimum");
            Integer maximum = privateConfig.getInteger("maximum");
            valid.valid4Integer(minimum, maximum);
            return null;
        } else if (type.equals("float")) {
            BigDecimal minimum = privateConfig.getBigDecimal("minimum");
            BigDecimal maximum = privateConfig.getBigDecimal("maximum");
            Integer minIntLen = privateConfig.getInteger("minIntLen");
            Integer maxIntLen = privateConfig.getInteger("maxIntLen");
            Integer minDecLen = privateConfig.getInteger("minDecLen");
            Integer maxDecLen = privateConfig.getInteger("maxDecLen");
            valid.valid4Float(minimum, maximum, minIntLen, maxIntLen, minDecLen, maxDecLen);
            return null;
        } else if (type.equals("InDb") || type.equals("NotInDb")) {
            Integer dbId = privateConfig.getInteger("dbId");
            String table = privateConfig.getString("table");
            JSONObject columnInfo = privateConfig.getJSONObject("column");
            String columnName = columnInfo.getString("name");
            String columnType = columnInfo.getString("type");
            valid.valid4DbData(dbId, table, columnName, columnType);
            return null;
        } else if (type.equals("const")) {
            Object cst = privateConfig.get("const");
            valid.valid4Const(cst);
            return null;
        } else if (type.equals("inArray") || type.equals("notInArray")) {
            String arrayType = privateConfig.getString("type");
            JSONArray arrayValue = privateConfig.getJSONArray("value");
            valid.valid4ArrayData(arrayType, arrayValue);
            return null;
        } else {
            throw new ValidException("unknown type : " + type);
        }
    }

    /**
     * 生成字段类型为String的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param publicConfig 全局配置
     * @param allowIllegal 是否允许非法字符
     * @param allowEmpty 是否运行为空
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 字段用例
     */
    private JSONArray genString(String key, String desc,
                           JSONObject publicConfig,
                           Boolean allowIllegal, Boolean allowEmpty, Integer minLen, Integer maxLen) {

        Boolean allowNull = publicConfig.getBoolean("allowNull");
        Boolean allowRepeat = publicConfig.getBoolean("allowRepeat");

        JSONArray result = new JSONArray();

        //1.null
        if (allowNull) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null));
        }

        //2.empty
        if (allowEmpty) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Empty(key, desc), ""));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Empty(key, desc), ""));
        }

        //3.长度
        //A.长度范围内
        String randomLegalString = RandomUtil.randomLegalString(minLen, maxLen);
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Length(key, desc, minLen, maxLen), RandomUtil.randomLegalString(minLen, maxLen)));

        //B.恰好为最大长度
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4EqualsMaxLength(key, desc, maxLen), RandomUtil.randomLegalStringByLength(maxLen)));

        //C.恰好为最小长度
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4EqualsMinLength(key, desc, minLen), RandomUtil.randomLegalStringByLength(minLen)));

        //D.恰好为最大长度+1
        result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4GreaterLength(key, desc, maxLen, 1), RandomUtil.randomLegalStringByLength(maxLen + 1)));

        //E.恰好为最小长度-1
        result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4LessLength(key, desc, minLen, 1), RandomUtil.randomLegalStringByLength(minLen - 1)));

        //4.非法字符
        if (allowIllegal) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Length(key, desc, minLen, maxLen), RandomUtil.randomIllegalString(minLen, maxLen)));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Length(key, desc, minLen, maxLen), RandomUtil.randomIllegalString(minLen, maxLen)));
        }

        //5.重复
        if (allowRepeat) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4LengthRepeat(key, desc, minLen, maxLen), randomLegalString));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4LengthRepeat(key, desc, minLen, maxLen), randomLegalString));
        }

        return result;
    }

    /**
     * 单个用例格式封装
     * @param type 类型
     * @param desc 描述
     * @param value 值
     * @return json对象
     */
    private JSONObject model(CaseType type, String desc, Object value) {
        JSONObject jsonObject = new JSONObject();
        if (type == CaseType.INVALID_EQUIVALENCE_CLASS) {
            jsonObject.put("type", "invalidEquivalenceClass");
        } else {
            jsonObject.put("type", "validEquivalenceClass");
        }
        jsonObject.put("desc", desc);
        jsonObject.put("value", value);
        return jsonObject;
    }
}
