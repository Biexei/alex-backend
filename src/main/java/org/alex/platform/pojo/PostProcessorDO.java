package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class PostProcessorDO implements Serializable {
    private Integer postProcessorId;
    @NotNull(message = "测试用例编号不能为空")
    private Integer caseId;
    @NotNull(message = "后置处理器名称不能为空")
    @NotEmpty(message = "后置处理器名称不能为空")
    @Size(max = 50, message = "后置处理器名称必须小于等于50")
    private String name;
    @NotNull(message = "后置处理器提取类型不能为空")
    @Max(value = 6, message = "后置处理器提取类型方式为0~6")
    @Min(value = 0, message = "后置处理器提取类型方式为0~6")
    private Byte type;
    @NotNull(message = "后置处理器提取表达式不能为空")
    @NotEmpty(message = "后置处理器提取表达式不能为空")
    @Size(max = 50, message = "后置处理器提取表达式必须小于等于50")
    private String expression;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @Size(max = 100, message = "缺省值必须小于等于100")
    @NotNull(message = "是否存在缺省值不能为空")
    @Max(value = 1, message = "是否存在缺省值为0~1")
    @Min(value = 0, message = "是否存在缺省值0~1")
    private Byte haveDefaultValue;
    private String defaultValue;

    public Byte getHaveDefaultValue() {
        return haveDefaultValue;
    }

    public void setHaveDefaultValue(Byte haveDefaultValue) {
        this.haveDefaultValue = haveDefaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getPostProcessorId() {
        return postProcessorId;
    }

    public void setPostProcessorId(Integer postProcessorId) {
        this.postProcessorId = postProcessorId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
