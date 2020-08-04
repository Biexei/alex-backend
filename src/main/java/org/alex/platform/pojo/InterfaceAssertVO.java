package org.alex.platform.pojo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class InterfaceAssertVO implements Serializable {
    private Integer assertId;
    private String assertName;
    private Byte type;
    private String expression;
    private Byte operator;
    private String exceptedResult;
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
        return "AssertVO{" +
                "assertId=" + assertId +
                ", assertName='" + assertName + '\'' +
                ", type=" + type +
                ", expression='" + expression + '\'' +
                ", operator=" + operator +
                ", exceptedResult='" + exceptedResult + '\'' +
                ", order=" + order +
                '}';
    }
}
