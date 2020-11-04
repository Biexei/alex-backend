package org.alex.platform.pojo;

public class InterfaceAssertLogListVO {
    private String assertName;
    private Byte type;
    private String expression;
    private Byte operator;
    private String exceptedResult;
    private Integer order;
    private String actualResult;
    private Byte status;
    private String errorMessage;
    private String rawExceptedResult;

    public String getRawExceptedResult() {
        return rawExceptedResult;
    }

    public void setRawExceptedResult(String rawExceptedResult) {
        this.rawExceptedResult = rawExceptedResult;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
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
}
