package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class InterfaceAssertLogDO implements Serializable {
    private Integer assertLogId;
    private Integer executeLogId;
    private Integer assertId;
    private Integer caseId;
    private String assertName;
    private Byte type;
    private String expression;
    private Byte operator;
    private String exceptedResult;
    private String actualResult;
    private Integer order;
    private Byte status;
    private String errorMessage;
    private Date createdTime;
    private String rawExceptedResult;

    public String getRawExceptedResult() {
        return rawExceptedResult;
    }

    public void setRawExceptedResult(String rawExceptedResult) {
        this.rawExceptedResult = rawExceptedResult;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getAssertLogId() {
        return assertLogId;
    }

    public void setAssertLogId(Integer assertLogId) {
        this.assertLogId = assertLogId;
    }

    public Integer getExecuteLogId() {
        return executeLogId;
    }

    public void setExecuteLogId(Integer executeLogId) {
        this.executeLogId = executeLogId;
    }

    public Integer getAssertId() {
        return assertId;
    }

    public void setAssertId(Integer assertId) {
        this.assertId = assertId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getAssertName() {
        return assertName;
    }

    public void setAssertName(String assertName) {
        this.assertName = assertName;
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

    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "InterfaceAssertLogDO{" +
                "assertLogId=" + assertLogId +
                ", executeLogId=" + executeLogId +
                ", assertId=" + assertId +
                ", caseId=" + caseId +
                ", assertName='" + assertName + '\'' +
                ", type=" + type +
                ", expression='" + expression + '\'' +
                ", operator=" + operator +
                ", exceptedResult='" + exceptedResult + '\'' +
                ", actualResult='" + actualResult + '\'' +
                ", order=" + order +
                ", status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
