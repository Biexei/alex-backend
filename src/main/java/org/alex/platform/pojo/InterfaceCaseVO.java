package org.alex.platform.pojo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class InterfaceCaseVO {
    private Integer moduleId;
    private String moduleName;
    private Integer projectId;
    private String projectName;
    private Integer caseId;
    private String url;
    private Byte method;
    private String desc;
    private Byte level;
    private String doc;
    private String headers;
    private String params;
    private String data;
    private String json;
    private Integer creater;
    private Date createdTime;
    private Date updateTime;
}
