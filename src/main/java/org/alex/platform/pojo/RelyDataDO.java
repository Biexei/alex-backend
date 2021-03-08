package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class RelyDataDO implements Serializable {
    private Integer id;
    @NotEmpty(message = "依赖名称不允许为空")
    @Size(min = 1, max = 100, message = "依赖名称长度必须为[1,100]")
    @Pattern(regexp = "\\w+", message = "依赖名称必须为字母数字下划线")
    private String name;
    @NotEmpty(message = "固定值/方法声明/SQL不允许为空")
    @Size(min = 1, max = 100, message = "固定值/方法声明/SQL长度必须为[1,100]")
    private String value;
    @NotEmpty(message = "依赖描述不允许为空")
    @Size(min = 1, max = 100, message = "依赖描述长度必须为[1,100]")
    private String desc;
    @NotNull(message = "类型不允许为空")
    private Byte type;
    private Integer datasourceId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Integer datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
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
