package org.alex.platform.enums;

public enum RelyType {
    INVOKE, //反射方法
    PRE_CASE, //前置用例
    INTERFACE_JSON, //接口依赖-JSON
    INTERFACE_HTML, //接口依赖-HTML
    INTERFACE_HEADER, //接口依赖-HEADER
    SQL_SELECT, //sql查询
    SQL_DELETE, //sql删除
    SQL_INSERT, //sql新增
    SQL_UPDATE, //sql查询
    SQL_SCRIPT, //sql脚本
    CONST, //固定字符
    END, //最后一个节点 调用自身
}
