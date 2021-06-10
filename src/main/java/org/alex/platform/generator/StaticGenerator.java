package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.enums.CaseType;
import org.alex.platform.enums.ResultType;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.DbVO;
import org.alex.platform.service.DbService;
import org.alex.platform.util.JdbcUtil;
import org.alex.platform.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * 静态生成测试用例
 */
@Component
public class StaticGenerator implements Generator {
    @Autowired
    Filter filter;
    @Autowired
    Description description;
    @Autowired
    DbService dbService;

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
    public JSONArray genString(String key, String desc, Boolean allowIllegal, Boolean allowEmpty, Integer minLen,
                                Integer maxLen, Boolean allowNull) {

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
        String randomLegalString = RandomUtil.randomLegalString(minLen, maxLen);
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Length(key, desc, minLen, maxLen), randomLegalString, key));

        //B.恰好为最大长度
        String randomMax = RandomUtil.randomLegalStringByLength(maxLen);
        while (randomMax.equals(randomLegalString)) {
            randomMax = RandomUtil.randomLegalStringByLength(maxLen);
        }
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4EqualsMaxLength(key, desc, maxLen), randomMax, key));

        //C.恰好为最小长度
        String randomMin = RandomUtil.randomLegalStringByLength(minLen);
        while (randomMin.equals(randomLegalString) || randomMin.equals(randomMax)) {
            randomMin = RandomUtil.randomLegalStringByLength(minLen);
        }
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4EqualsMinLength(key, desc, minLen), randomMin, key));

        //D.恰好为最大长度+1
        result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4GreaterLength(key, desc, maxLen, 1), RandomUtil.randomLegalStringByLength(maxLen + 1), key));

        //E.恰好为最小长度-1
        result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4LessLength(key, desc, minLen, 1), RandomUtil.randomLegalStringByLength(minLen - 1), key));

        if (allowIllegal != null) {
            //4.非法字符
            if (allowIllegal) {
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4IllegalLength(key, desc, minLen, maxLen), RandomUtil.randomIllegalString(minLen, maxLen), key));
            } else {
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4IllegalLength(key, desc, minLen, maxLen), RandomUtil.randomIllegalString(minLen, maxLen), key));
            }
        }
        return result;
    }

    /**
     * 生成字段类型为Number的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param min 最小值
     * @param max 最大值
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
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

    /**
     * 生成字段类型为InDb的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param dbId 数据源编号
     * @param sql sql查询语句
     * @param elementType 列字段类型
     * @param allowNull 是否运行为空
     * @return 字段用例
     */
    @Override
    public JSONArray genInDb(String key, String desc, Integer dbId, String sql, String elementType,
                              Boolean allowNull) throws Exception {

        JSONArray result = new JSONArray();

        DbVO db = dbService.findDbById(dbId);
        String url = db.getUrl();
        String username = db.getUsername();
        String password = db.getPassword();

        String resultType = ResultType.getResultType(elementType);

        // 0.null
        if (allowNull) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        }

        switch (resultType) {
            case "string": {
                List<String> rows = JdbcUtil.queryForList(url, username, password, sql, String.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                String randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.有效等价类-在表内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.无效等价类-不在表内
                String invalidStr = RandomUtil.randomLegalStringByLength(randomRow.length());
                while (randomRow.equals(invalidStr)) {
                    invalidStr = RandomUtil.randomLegalStringByLength(randomRow.length());
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), invalidStr, key));
                break;
            }
            case "number": {
                List<BigDecimal> rows = JdbcUtil.queryForList(url, username, password, sql, BigDecimal.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                BigDecimal randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.有效等价类-在表内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.无效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal invalidNum = RandomUtil.randomBigDecimal(min, max);
                while (randomRow.equals(invalidNum)) {
                    invalidNum = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), invalidNum, key));
                break;
            }
            case "integer": {
                List<Integer> rows = JdbcUtil.queryForList(url, username, password, sql, Integer.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                Integer randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.有效等价类-在表内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.无效等价类-不在表内
                int min = 0;
                int max = 999999;
                int invalidInt = RandomUtil.randomInt(min, max);
                while (randomRow.equals(invalidInt)) {
                    invalidInt = RandomUtil.randomInt(min, max);
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), invalidInt, key));
                break;
            }
            case "float": {
                List<Float> rows = JdbcUtil.queryForList(url, username, password, sql, Float.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                Float randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.有效等价类-在表内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.无效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal invalidFloat = RandomUtil.randomBigDecimal(min, max);
                while (randomRow.equals(invalidFloat.floatValue())) {
                    invalidFloat = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), invalidFloat, key));
                break;
            }
            default: {
                List<Double> rows = JdbcUtil.queryForList(url, username, password, sql, Double.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                Double randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.有效等价类-在表内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.无效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal invalidDouble = RandomUtil.randomBigDecimal(min, max);
                while (randomRow.equals(invalidDouble.doubleValue())) {
                    invalidDouble = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), invalidDouble, key));
                break;
            }
        }
        return result;
    }

    /**
     * 生成字段类型为NotInDb的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param dbId 数据源编号
     * @param sql sql查询语句
     * @param elementType 列字段类型
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
    @Override
    public JSONArray genNotInDb(String key, String desc, Integer dbId, String sql, String elementType,
                                 Boolean allowNull) throws Exception {

        JSONArray result = new JSONArray();

        DbVO db = dbService.findDbById(dbId);
        String url = db.getUrl();
        String username = db.getUsername();
        String password = db.getPassword();

        String resultType = ResultType.getResultType(elementType);

        // 0.null
        if (allowNull) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        }

        switch (resultType) {
            case "string": {
                List<String> rows = JdbcUtil.queryForList(url, username, password, sql, String.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                String randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.无效等价类-在表内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.有效等价类-不在表内
                String validStr = RandomUtil.randomLegalStringByLength(randomRow.length());
                while (randomRow.equals(validStr)) {
                    validStr = RandomUtil.randomLegalStringByLength(randomRow.length());
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), validStr, key));
                break;
            }
            case "number": {
                List<BigDecimal> rows = JdbcUtil.queryForList(url, username, password, sql, BigDecimal.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                BigDecimal randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.无效等价类-在表内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.有效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal validNum = RandomUtil.randomBigDecimal(min, max);
                while (randomRow.equals(validNum)) {
                    validNum = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), validNum, key));
                break;
            }
            case "integer": {
                List<Integer> rows = JdbcUtil.queryForList(url, username, password, sql, Integer.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                Integer randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.无效等价类-在表内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.有效等价类-不在表内
                int min = 0;
                int max = 999999;
                int validInt = RandomUtil.randomInt(min, max);
                while (randomRow.equals(validInt)) {
                    validInt = RandomUtil.randomInt(min, max);
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), validInt, key));
                break;
            }
            case "float": {
                List<Float> rows = JdbcUtil.queryForList(url, username, password, sql, Float.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                Float randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.无效等价类-在表内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.有效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal validFloat = RandomUtil.randomBigDecimal(min, max);
                while (randomRow.equals(validFloat.floatValue())) {
                    validFloat = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), validFloat, key));
                break;
            }
            default: {
                List<Double> rows = JdbcUtil.queryForList(url, username, password, sql, Double.class);
                if (rows.isEmpty()) {
                    throw new BusinessException("查询结果为空");
                }
                Double randomRow = rows.get(new Random().nextInt(rows.size()));
                // 1.无效等价类-在表内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InDb(key, desc), randomRow, key));
                // 2.有效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal validDouble = RandomUtil.randomBigDecimal(min, max);
                while (randomRow.equals(validDouble.doubleValue())) {
                    validDouble = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInDb(key, desc), validDouble, key));
                break;
            }
        }
        return result;
    }

    /**
     * 生成字段类型为const的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param value 固定值
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
    @Override
    public JSONArray genConst(String key, String desc, Object value, Boolean allowNull) {

        JSONArray result = new JSONArray();

        // 0.null
        if (allowNull) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        }

        // 有效等价类
        result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Const(key, desc, value), value, key));

        return result;
    }

    /**
     * 生成字段类型为InArray的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param array 固定值
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
    @Override
    public JSONArray genInArray(String key, String desc, JSONArray array, String elementType,
                                 Boolean allowNull) throws ValidException {

        JSONArray result = new JSONArray();

        // 0.null
        if (allowNull) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        }

        String resultType = ResultType.getResultType(elementType);

        switch (resultType) {
            case "string": {
                String randomRow = array.getString(new Random().nextInt(array.size()));
                // 1.有效等价类-在数组内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.无效等价类-不在数组内
                String invalidStr = RandomUtil.randomLegalStringByLength(randomRow.length());
                while (array.contains(invalidStr)) {
                    invalidStr = RandomUtil.randomLegalStringByLength(randomRow.length());
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), invalidStr, key));
                break;
            }
            case "number": {
                BigDecimal randomRow = array.getBigDecimal(new Random().nextInt(array.size()));
                // 1.有效等价类-在数组内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.无效等价类-不在数组内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal invalidNum = RandomUtil.randomBigDecimal(min, max);
                while (array.contains(invalidNum)) {
                    invalidNum = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), invalidNum, key));
                break;
            }
            case "integer": {
                Integer randomRow = array.getInteger(new Random().nextInt(array.size()));
                // 1.有效等价类-在数组内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.无效等价类-不在数组内
                int min = 0;
                int max = 999999;
                int invalidInt = RandomUtil.randomInt(min, max);
                while (array.contains(invalidInt)) {
                    invalidInt = RandomUtil.randomInt(min, max);
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), invalidInt, key));
                break;
            }
            case "float": {
                Float randomRow = array.getFloat(new Random().nextInt(array.size()));
                // 1.有效等价类-在表内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.无效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal invalidFloat = RandomUtil.randomBigDecimal(min, max);
                while (array.contains(invalidFloat.floatValue())) {
                    invalidFloat = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), invalidFloat, key));
                break;
            }
            default: {
                Double randomRow = array.getDouble(new Random().nextInt(array.size()));
                // 1.有效等价类-在表内
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.无效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal invalidDouble = RandomUtil.randomBigDecimal(min, max);
                while (array.contains(invalidDouble.floatValue())) {
                    invalidDouble = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), invalidDouble, key));
                break;
            }
        }
        return result;
    }

    /**
     * 生成字段类型为NotInArray的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param array 固定值
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
    @Override
    public JSONArray genNotInArray(String key, String desc, JSONArray array, String elementType,
                                    Boolean allowNull) throws ValidException {

        JSONArray result = new JSONArray();

        // 0.null
        if (allowNull) {
            result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        } else {
            result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4Null(key, desc), null, key));
        }

        String resultType = ResultType.getResultType(elementType);

        switch (resultType) {
            case "string": {
                String randomRow = array.getString(new Random().nextInt(array.size()));
                // 1.无效等价类-在数组内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.有效等价类-不在数组内
                String validStr = RandomUtil.randomLegalStringByLength(randomRow.length());
                while (array.contains(validStr)) {
                    validStr = RandomUtil.randomLegalStringByLength(randomRow.length());
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), validStr, key));
                break;
            }
            case "number": {
                BigDecimal randomRow = array.getBigDecimal(new Random().nextInt(array.size()));
                // 1.无效等价类-在数组内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.有效等价类-不在数组内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal validNum = RandomUtil.randomBigDecimal(min, max);
                while (array.contains(validNum)) {
                    validNum = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), validNum, key));
                break;
            }
            case "integer": {
                Integer randomRow = array.getInteger(new Random().nextInt(array.size()));
                // 1.无效等价类-在数组内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.有效等价类-不在数组内
                int min = 0;
                int max = 999999;
                int validInt = RandomUtil.randomInt(min, max);
                while (array.contains(validInt)) {
                    validInt = RandomUtil.randomInt(min, max);
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), validInt, key));
                break;
            }
            case "float": {
                Float randomRow = array.getFloat(new Random().nextInt(array.size()));
                // 1.无效等价类-在表内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.有效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal validFloat = RandomUtil.randomBigDecimal(min, max);
                while (array.contains(validFloat.floatValue())) {
                    validFloat = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), validFloat, key));
                break;
            }
            default: {
                Double randomRow = array.getDouble(new Random().nextInt(array.size()));
                // 1.有效等价类-在表内
                result.add(model(CaseType.INVALID_EQUIVALENCE_CLASS, description.desc4InArray(key, desc), randomRow, key));
                // 2.无效等价类-不在表内
                BigDecimal min = new BigDecimal("0.00");
                BigDecimal max = new BigDecimal("99999.99");
                BigDecimal validDouble = RandomUtil.randomBigDecimal(min, max);
                while (array.contains(validDouble.doubleValue())) {
                    validDouble = RandomUtil.randomBigDecimal(min, max);
                }
                result.add(model(CaseType.VALID_EQUIVALENCE_CLASS, description.desc4NotInArray(key, desc), validDouble, key));
                break;
            }
        }
        return result;
    }
}
