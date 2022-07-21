package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class InterfaceAssertDO implements Serializable {
    private Integer assertId;
    @NotEmpty(message = "断言名称不能为空")
    @Size(max = 100, message = "断言长度必须小于100")
    private String assertName;
    @NotNull(message = "用例编号不能为空")
    private Integer caseId;
    @NotNull(message = "提取数据类型不能为空")
    @Max(value = 4, message = "提取数据类型必须为0~4")
    @Min(value = 0, message = "提取数据类型必须为0~4")
    private Byte type;
    @NotNull(message = "提取表达式不能为空")
    @NotEmpty(message = "提取表达式不能为空")
    @Size(max = 50, message = "提取表达式长度必须小于50")
    private String expression;
    @NotNull(message = "操作符不能为空")
    @Max(value = 12, message = "操作符必须为0~12")
    @Min(value = 0, message = "操作符必须为0~12")
    private Byte operator;
    @Size(max = 1000, message = "预期结果长度必须小于1000")
    private String exceptedResult;
    @NotNull(message = "排序不能为空")
    private Integer order;
    @DateTimeFormat(pattern = "yyyy-MM-dd:HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd:HH:mm:ss")
    private Date updateTime;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

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
