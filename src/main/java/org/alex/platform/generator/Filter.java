package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import org.alex.platform.enums.ResultType;
import org.alex.platform.exception.ValidException;
import org.alex.platform.service.DbService;
import org.alex.platform.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 检查各类型输入数据的完整性
 */
@Component
public class Filter {
    @Autowired
    DbService dbService;

    public void valid4String(Boolean allowIllegal, Boolean allowEmpty, Integer minLen, Integer maxLen) throws ValidException {
        ValidUtil.notNUll(allowIllegal, "type of String field valid error, allowIllegal should not be null");
        ValidUtil.notNUll(allowEmpty, "type of String field valid error, allowEmpty should not be null");
        ValidUtil.notNUll(minLen, "type of String field valid error, minLen should not be null");
        ValidUtil.notNUll(maxLen, "type of String field valid error, maxLen should not be null");
        ValidUtil.isGreaterThanOrEqualsZero(minLen, "type of String field valid error, minLen must greater than or equals 0");
        ValidUtil.isGreaterThanOrEqualsZero(maxLen, "type of String field valid error, maxLen must greater than or equals 0");
        if (minLen.compareTo(maxLen) > 0) {
            throw new ValidException("type of String field valid error, maxLen should greater than or equals minLen");
        }
    }

    public void valid4Number(BigDecimal min, BigDecimal max) throws ValidException {
        ValidUtil.notNUll(min, "type of Number field valid error, min should not be null");
        ValidUtil.notNUll(max, "type of Number field valid error, max should not be null");
        if (min.compareTo(max) > 0) {
            throw new ValidException("type of Number field valid error, max should greater than or equals min");
        }
    }

    public void valid4DbData(Integer dbId, String sql, String elementType) throws ValidException {
        ValidUtil.notNUll(dbId, "type of DbData field valid error, dbId should not be null");
        ValidUtil.notNUll(sql, "type of DbData field valid error, sql should not be null");
        ValidUtil.notNUll(elementType, "type of DbData field valid error, elementType should not be null");
        // 检查查询返回值类型是否在枚举范围
        ResultType.getResultType(elementType);
        // 检查DbID是否存在于数据源管理表
        ValidUtil.notNUll(dbService.findDbById(dbId), "type of DbData field valid error, dbId not found");
    }

    public void valid4Const(Object cst) throws ValidException {
        ValidUtil.notNUll(cst, "type of Const field valid error, value should not be null");
    }

    public void valid4ArrayData(String arrayType, JSONArray arrayValue) throws ValidException {
        ValidUtil.notNUll(arrayType, "type of ArrayData field valid error, elementType should not be null");
        ValidUtil.notEmpty(arrayValue, "type of ArrayData field valid error, value should not be empty");
        ValidUtil.notNUll(arrayValue, "type of ArrayData field valid error, value should not be null");
        // 检查数组元素类型是否在枚举范围
        ResultType.getResultType(arrayType);
    }

    public void valid4GlobalConfig(Boolean allowNull, Boolean allowRepeat) throws ValidException{
        ValidUtil.notNUll(allowNull, "type of GlobalConfig field valid error, allowNull should not be null");
        ValidUtil.notNUll(allowRepeat, "type of GlobalConfig field valid error, allowRepeat should not be null");
    }
}
