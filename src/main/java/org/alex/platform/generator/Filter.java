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

    public void valid4String(Boolean allowEmpty, Integer minLen, Integer maxLen, Boolean allowNull) throws ValidException {
        ValidUtil.notNUll(allowEmpty, "String field valid error, allowEmpty should not be null");
        ValidUtil.notNUll(minLen, "String field valid error, minLen should not be null");
        ValidUtil.notNUll(maxLen, "String field valid error, maxLen should not be null");
        ValidUtil.isGreaterThanOrEqualsZero(minLen, "String field valid error, minLen must greater than or equals 0");
        ValidUtil.isGreaterThanOrEqualsZero(maxLen, "String field valid error, maxLen must greater than or equals 0");
        ValidUtil.notNUll(allowNull, "String field valid error, allowNull should not be null");
        if (minLen.compareTo(maxLen) > 0) {
            throw new ValidException("String field valid error, maxLen should greater than or equals minLen");
        }
    }

    public void valid4Number(BigDecimal min, BigDecimal max, Boolean allowNull) throws ValidException {
        ValidUtil.notNUll(min, "Number field valid error, min should not be null");
        ValidUtil.notNUll(max, "Number field valid error, max should not be null");
        ValidUtil.notNUll(allowNull, "Number field valid error, allowNull should not be null");
        if (min.compareTo(max) > 0) {
            throw new ValidException("Number field valid error, max should greater than or equals min");
        }
    }

    public void valid4DbData(Integer dbId, String sql, String elementType, Boolean allowNull) throws ValidException {
        ValidUtil.notNUll(dbId, "DbData field valid error, dbId should not be null");
        ValidUtil.notNUll(sql, "DbData field valid error, sql should not be null");
        ValidUtil.notNUll(elementType, "DbData field valid error, elementType should not be null");
        ValidUtil.notNUll(allowNull, "DbData field valid error, allowNull should not be null");
        // 检查查询返回值类型是否在枚举范围
        ResultType.getResultType(elementType);
        // 检查DbID是否存在于数据源管理表
        ValidUtil.notNUll(dbService.findDbById(dbId), "DbData field valid error, dbId not found");
    }

    public void valid4Const(Object cst, Boolean allowNull) throws ValidException {
        ValidUtil.notNUll(allowNull, "Const field valid error, allowNull should not be null");
        ValidUtil.notNUll(cst, "Const field valid error, value should not be null");
    }

    public void valid4ArrayData(String arrayType, JSONArray arrayValue, Boolean allowNull) throws ValidException {
        ValidUtil.notNUll(allowNull, "ArrayData field valid error, allowNull should not be null");
        ValidUtil.notNUll(arrayType, "ArrayData field valid error, elementType should not be null");
        ValidUtil.notEmpty(arrayValue, "ArrayData field valid error, value should not be empty");
        ValidUtil.notNUll(arrayValue, "ArrayData field valid error, value should not be null");
        // 检查数组元素类型是否在枚举范围
        ResultType.getResultType(arrayType);
    }
}
