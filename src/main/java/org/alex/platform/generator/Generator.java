package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.enums.CaseType;
import org.alex.platform.exception.ValidException;

import java.math.BigDecimal;

public interface Generator {

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
    JSONArray genString(String key, String desc, Boolean allowIllegal, Boolean allowEmpty, Integer minLen,
                        Integer maxLen, Boolean allowNull);

    /**
     * 生成字段类型为Number的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param min 最小值
     * @param max 最大值
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
    JSONArray genNumber(String key, String desc, BigDecimal min, BigDecimal max, Boolean allowNull);

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
    JSONArray genInDb(String key, String desc, Integer dbId, String sql, String elementType, Boolean allowNull)
                        throws Exception;

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
    JSONArray genNotInDb(String key, String desc, Integer dbId, String sql, String elementType, Boolean allowNull)
                        throws Exception;

    /**
     * 生成字段类型为const的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param value 固定值
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
    JSONArray genConst(String key, String desc, Object value, Boolean allowNull);

    /**
     * 生成字段类型为InArray的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param array 固定值
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
    JSONArray genInArray(String key, String desc, JSONArray array, String elementType,
                         Boolean allowNull) throws ValidException;

    /**
     * 生成字段类型为NotInArray的用例
     * @param key 字段名称
     * @param desc 字段描述
     * @param array 固定值
     * @param allowNull 是否允许为空
     * @return 字段用例
     */
    JSONArray genNotInArray(String key, String desc, JSONArray array, String elementType,
                            Boolean allowNull) throws ValidException;

    /**
     * 单个用例格式封装
     * @param type 类型
     * @param desc 描述
     * @param value 值
     * @return json对象
     */
     default JSONObject model(CaseType type, String desc, Object value, String key) {
        JSONObject jsonObject = new JSONObject();
        if (type == CaseType.INVALID_EQUIVALENCE_CLASS) {
            jsonObject.put("type", "invalidEquivalenceClass");
            jsonObject.put("desc", "!" + desc);
        } else {
            jsonObject.put("desc", desc);
            jsonObject.put("type", "validEquivalenceClass");
        }
        jsonObject.put("value", value);
        jsonObject.put("key", key);
        return jsonObject;
    }

    /**
     * 生成边界值
     * @param min 最小值
     * @return 最小值-步长的边界值
     */
    default JSONObject getLessThanMinNum(BigDecimal min) {
        JSONObject jsonObject = new JSONObject();
        String bigDecimalStr = min.toString();
        int i = bigDecimalStr.indexOf(".");
        if (i == -1) {
            jsonObject.put("num", min.subtract(new BigDecimal(1)));
            jsonObject.put("step", new BigDecimal(1));
        } else {
            int length = bigDecimalStr.substring(i + 1).length();
            StringBuilder patten = new StringBuilder("0.");
            for (int j = 0; j < length - 1; j++) {
                patten.append(0);
            }
            patten.append(1);
            String pattenStr = patten.toString();
            jsonObject.put("num", min.subtract(new BigDecimal(pattenStr)));
            jsonObject.put("step", new BigDecimal(pattenStr));
        }
        return jsonObject;
    }

    /**
     * 生成边界值
     * @param max 最大值
     * @return 最大值-步长的边界值
     */
    default JSONObject getGreaterThanMaxNum(BigDecimal max) {
        JSONObject jsonObject = new JSONObject();
        String bigDecimalStr = max.toString();
        int i = bigDecimalStr.indexOf(".");
        if (i == -1) {
            jsonObject.put("num", max.add(new BigDecimal(1)));
            jsonObject.put("step", new BigDecimal(1));
        } else {
            int length = bigDecimalStr.substring(i + 1).length();
            StringBuilder patten = new StringBuilder("0.");
            for (int j = 0; j < length - 1; j++) {
                patten.append(0);
            }
            patten.append(1);
            String pattenStr = patten.toString();
            jsonObject.put("num", max.add(new BigDecimal(pattenStr)));
            jsonObject.put("step", new BigDecimal(pattenStr));
        }
        return jsonObject;
    }

    /**
     * 根据方法名称和参数，生成调用函数字符串
     * @param name 方法名称
     * @param params 动态参数
     * @return ${xx()}
     */
    default String function(String name, Object... params) {
        if (params.length == 0) {
            return String.format("${%s()}", name);
        }
        StringBuilder p = new StringBuilder();
        for (Object paramObject : params) {
            String param = paramObject.toString();
            p.append("'").append(param).append("', ");
        }
        p = new StringBuilder(p.substring(0, p.length() - 2));
        return String.format("${%s(%s)}", name, p.toString());
    }

    /**
     * 根据方法名称和参数，生成调用函数字符串
     * @param name 方法名称
     * @param params 动态参数
     * @return ${xx()}
     */
    default String function(String name, String... params) {
        if (params.length == 0) {
            return String.format("${%s()}", name);
        }
        StringBuilder p = new StringBuilder();
        for (String param : params) {
            p.append("'").append(param).append("', ");
        }
        p = new StringBuilder(p.substring(0, p.length() - 2));
        return String.format("${%s(%s)}", name, p.toString());
    }
}
