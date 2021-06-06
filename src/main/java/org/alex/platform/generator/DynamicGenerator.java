package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.enums.CaseType;
import org.alex.platform.exception.ValidException;
import org.alex.platform.service.DbService;
import org.alex.platform.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DynamicGenerator implements Generator{
    @Autowired
    Filter filter;
    @Autowired
    Description description;
    @Autowired
    DbService dbService;

    @Override
    public JSONArray genSingleField(String key, String desc, String type, JSONObject config) throws Exception {
        return null;
    }

    /**
     * 生成字段类型为String的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param allowIllegal 是否允许非法字符
     * @param allowEmpty 是否允许为空
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @param allowNull 是否运行为空
     * @return 字段用例
     */
    @Override
    public JSONArray genString(String key, String desc, Boolean allowIllegal, Boolean allowEmpty, Integer minLen, Integer maxLen, Boolean allowNull) {

        JSONArray result = new JSONArray();

        //1.null
        if (allowNull) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        }

        //2.empty
        if (allowEmpty) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Empty(key, desc), "", key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Empty(key, desc), "", key));
        }

        //3.长度
        //A.长度范围内
        String randomLegalString = this.function("randomLegal", minLen, maxLen);
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Length(key, desc, minLen, maxLen), randomLegalString, key));

        //B.恰好为最大长度
        String randomMax = this.function("randomLegal", maxLen);
        while (randomMax.equals(randomLegalString)) {
            randomMax = RandomUtil.randomLegalStringByLength(maxLen);
        }
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4EqualsMaxLength(key, desc, maxLen), randomMax, key));

        //C.恰好为最小长度
        String randomMin = this.function("randomLegal", minLen);
        while (randomMin.equals(randomLegalString) || randomMin.equals(randomMax)) {
            randomMin = RandomUtil.randomLegalStringByLength(minLen);
        }
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4EqualsMinLength(key, desc, minLen), randomMin, key));

        //D.恰好为最大长度+1
        String randomMaxAddOne = this.function("randomLegal", maxLen + 1);
        result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4GreaterLength(key, desc, maxLen, 1), randomMaxAddOne, key));

        //E.恰好为最小长度-1
        String randomMinSubOne = this.function("randomLegal", minLen + 1);
        result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4LessLength(key, desc, minLen, 1), randomMinSubOne, key));

        //4.非法字符
        String randomIllegalString = this.function("randomIllegal", minLen, maxLen);
        if (allowIllegal) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4IllegalLength(key, desc, minLen, maxLen), randomIllegalString, key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4IllegalLength(key, desc, minLen, maxLen), randomIllegalString, key));
        }

        return result;
    }

    @Override
    public JSONArray genNumber(String key, String desc, BigDecimal min, BigDecimal max, Boolean allowNull) {

        JSONArray result = new JSONArray();

        // 1.null
        if (allowNull) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        }
        // 2.恰好为最大值
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4EqualsMaxSize(key, desc, max), max, key));
        if (min.compareTo(max) < 0) { // 最大值>最小值，使用区间内值作为重复值
            // 3.恰好为最小值
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4EqualsMinSize(key, desc, min), min, key));
            // 找五次不等于最小值且不等于最大值的区间值
            BigDecimal bigDecimal = RandomUtil.randomBigDecimal(min, max);
            for (int i = 0; i < 5; i++) {
                if (bigDecimal.equals(min) && bigDecimal.equals(max)) {
                    bigDecimal = RandomUtil.randomBigDecimal(min, max);
                    break;
                }
            }
            // 4.最大值最小值区间内
            if (!bigDecimal.equals(min) && !bigDecimal.equals(max)) {
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Size(key, desc, min, max), bigDecimal, key));
            }
        }
        // 5.恰好为最小值-步长
        JSONObject minObject = getLessThanMinNum(min);
        result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4LessSize(key, desc, min, (BigDecimal)minObject.get("step")), minObject.get("num"), key));
        // 6.恰好为最大值+步长
        JSONObject maxObject = getGreaterThanMaxNum(max);
        result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4GreaterSize(key, desc, max, (BigDecimal)maxObject.get("step")), maxObject.get("num"), key));
        return result;
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
