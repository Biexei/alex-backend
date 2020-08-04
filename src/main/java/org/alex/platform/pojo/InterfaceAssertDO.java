package org.alex.platform.pojo;

import javax.validation.constraints.*;
import java.io.Serializable;

public class InterfaceAssertDO implements Serializable {
    private Integer assertId;
    @NotEmpty(message = "断言名称不能为空")
    @Size(max = 100, message = "断言长度必须小于100")
    private String assertName;
    @NotNull(message = "用例编号不能为空")
    private Integer caseId;
    @NotNull(message = "提取数据类型不能为空")
    @Max(value = 3, message = "提取数据类型必须为0~3")
    @Min(value = 0, message = "提取数据类型必须为0~3")
    private Byte type;
    @NotNull(message = "提取表达式不能为空")
    @Size(max = 50, message = "提取表达式长度必须小于50")
    private String expression;
    @NotNull(message = "操作符不能为空")
    @Max(value = 7, message = "操作符必须为0~7")
    @Min(value = 0, message = "操作符必须为0~7")
    private Byte operator;
    @Size(max = 1000, message = "预期结果长度必须小于50")
    @NotNull(message = "预期结果不能为空")
    private String exceptedResult;
    @NotNull(message = "排序不能为空")
    private Integer order;

    public Integer getAssertId() {
        return assertId;
    }

    public void setAssertId(Integer assertId) {
        this.assertId = assertId;
    }

    public String getAssertName() {
        return assertName;
    }

    public void setAssertName(String assertName) {
        this.assertName = assertName;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Byte getOperator() {
        return operator;
    }

    public void setOperator(Byte operator) {
        this.operator = operator;
    }

    public String getExceptedResult() {
        return exceptedResult;
    }

    public void setExceptedResult(String exceptedResult) {
        this.exceptedResult = exceptedResult;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "AssertDO{" +
                "assertId=" + assertId +
                ", assertName='" + assertName + '\'' +
                ", caseId=" + caseId +
                ", type=" + type +
                ", expression='" + expression + '\'' +
                ", operator=" + operator +
                ", exceptedResult='" + exceptedResult + '\'' +
                ", order=" + order +
                '}';
    }
}
