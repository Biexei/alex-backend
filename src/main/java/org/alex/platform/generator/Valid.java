package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import org.alex.platform.enums.FieldType;
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
public class Valid {
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

    public void valid4Number(BigDecimal minimum, BigDecimal maximum,
                                Integer minIntLen, Integer maxIntLen, Integer minDecLen, Integer maxDecLen) throws ValidException {
        if ((minimum == null || maximum == null) && (minIntLen != null && maxIntLen != null && minDecLen != null && maxDecLen != null)) {
            ValidUtil.isGreaterThanOrEqualsZero(minIntLen, "type of Number field valid error, minIntLen must greater than or equals 0");
            ValidUtil.isGreaterThanOrEqualsZero(maxIntLen, "type of Number field valid error, maxIntLen must greater than or equals 0");
            ValidUtil.isGreaterThanOrEqualsZero(minDecLen, "type of Number field valid error, minDecLen must greater than or equals 0");
            ValidUtil.isGreaterThanOrEqualsZero(maxDecLen, "type of Number field valid error, maxDecLen must greater than or equals 0");
            if (minIntLen.compareTo(maxIntLen) > 0) {
                throw new ValidException("type of Number field valid error, maxIntLen should greater than or equals minIntLen");
            }
            if (minDecLen.compareTo(maxDecLen) > 0) {
                throw new ValidException("type of Number field valid error, maxDecLen should greater than or equals minDecLen");
            }
        } else if ((minIntLen == null || maxIntLen == null || minDecLen == null || maxDecLen == null) && (minimum != null && maximum != null)) {
            ValidUtil.isGreaterThanOrEqualsZero(minimum, "type of Number field valid error, minimum must greater than or equals 0");
            ValidUtil.isGreaterThanOrEqualsZero(maximum, "type of Number field valid error, maximum must greater than or equals 0");
            if (minimum.compareTo(maximum) > 0) {
                throw new ValidException("type of Number field valid error, maximum should greater than or equals minimum");
            }
        } else {
            throw new ValidException("type of Number field valid error, confirm generate by size or length");
        }
    }

    public void valid4Integer(Integer minimum, Integer maximum) throws ValidException {
        ValidUtil.isGreaterThanOrEqualsZero(minimum, "type of Integer field valid error, minimum must greater than or equals 0");
        ValidUtil.isGreaterThanOrEqualsZero(maximum, "type of Integer field valid error, maximum must greater than or equals 0");
        if (minimum.compareTo(maximum) > 0) {
            throw new ValidException("type of Integer field valid error, maximum should greater than or equals minimum");
        }
    }

    public void valid4Float(BigDecimal minimum, BigDecimal maximum,
                            Integer minIntLen, Integer maxIntLen, Integer minDecLen, Integer maxDecLen) throws ValidException {
        if ((minimum == null || maximum == null) && (minIntLen != null && maxIntLen != null && minDecLen != null && maxDecLen != null)) {
            ValidUtil.isGreaterThanOrEqualsZero(minIntLen, "type of Float field valid error, minIntLen must greater than or equals 0");
            ValidUtil.isGreaterThanOrEqualsZero(maxIntLen, "type of Float field valid error, maxIntLen must greater than or equals 0");
            ValidUtil.isGreaterThanOrEqualsZero(minDecLen, "type of Float field valid error, minDecLen must greater than or equals 0");
            ValidUtil.isGreaterThanOrEqualsZero(maxDecLen, "type of Float field valid error, maxDecLen must greater than or equals 0");
            if (minIntLen.compareTo(maxIntLen) > 0) {
                throw new ValidException("type of Float field valid error, maxIntLen should greater than or equals minIntLen");
            }
            if (minDecLen.compareTo(maxDecLen) > 0) {
                throw new ValidException("type of Float field valid error, maxDecLen should greater than or equals minDecLen");
            }
        } else if ((minIntLen == null || maxIntLen == null || minDecLen == null || maxDecLen == null) && (minimum != null && maximum != null)) {
            ValidUtil.isGreaterThanOrEqualsZero(minimum, "type of Float field valid error, minimum must greater than or equals 0");
            ValidUtil.isGreaterThanOrEqualsZero(maximum, "type of Float field valid error, maximum must greater than or equals 0");
            if (minimum.compareTo(maximum) > 0) {
                throw new ValidException("type of Float field valid error, maximum should greater than or equals minimum");
            }
        } else {
            throw new ValidException("type of Float field valid error, confirm generate by size or length");
        }
    }

    public void valid4DbData(Integer dbId, String table, String columnName, String columnType) throws ValidException {
        ValidUtil.notNUll(dbId, "type of DbData field valid error, dbId should not be null");
        ValidUtil.notNUll(table, "type of DbData field valid error, table should not be null");
        ValidUtil.notNUll(columnName, "type of DbData field valid error, column.name should not be null");
        ValidUtil.notNUll(columnType, "type of DbData field valid error, column.type should not be null");
        // 检查查询返回值类型是否在枚举范围
        FieldType.getFieldType(columnType);
        // 检查DbID是否存在于数据源管理表
        ValidUtil.notNUll(dbService.findDbById(dbId), "type of DbData field valid error, dbId not found");
    }

    public void valid4Const(Object cst) throws ValidException {
        ValidUtil.notNUll(cst, "type of Const field valid error, value should not be null");
    }

    public void valid4ArrayData(String arrayType, JSONArray arrayValue) throws ValidException {
        ValidUtil.notNUll(arrayType, "type of ArrayData field valid error, type should not be null");
        ValidUtil.notNUll(arrayValue, "type of ArrayData field valid error, value should not be null");
        // 检查数组元素类型是否在枚举范围
        FieldType.getFieldType(arrayType);
    }

    public void valid4GlobalConfig(Boolean allowNull, Boolean allowRepeat) throws ValidException{
        ValidUtil.notNUll(allowNull, "type of GlobalConfig field valid error, allowNull should not be null");
        ValidUtil.notNUll(allowRepeat, "type of GlobalConfig field valid error, allowRepeat should not be null");
    }
}
