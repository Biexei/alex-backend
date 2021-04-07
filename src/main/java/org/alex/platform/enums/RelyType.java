package org.alex.platform.enums;

public enum RelyType {
    CASE_START, //用例开始执行
    CASE_END, //用例执行完成
    RELY_START, //依赖开始执行
    RELY_END, //依赖执行完成
    INVOKE, //反射方法
    PRE_CASE_START, //前置用例开始执行
    PRE_CASE_END, //前置用例执行完成
    INTERFACE_JSON, //接口依赖-JSON
    INTERFACE_HTML, //接口依赖-HTML
    INTERFACE_HEADER, //接口依赖-HEADER
    SQL_SELECT, //sql查询
    SQL_DELETE, //sql删除
    SQL_INSERT, //sql新增
    SQL_UPDATE, //sql查询
    SQL_SCRIPT, //sql脚本
    CONST, //固定字符
    READ_PROCESSOR, //读处理器
    WRITE_PROCESSOR_REQUEST_HEADER, //写处理器-请求头
    WRITE_PROCESSOR_REQUEST_PARAMS, //写处理器-请求头
    WRITE_PROCESSOR_REQUEST_DATA, //写处理器-请求头
    WRITE_PROCESSOR_REQUEST_JSON, //写处理器-请求头
    WRITE_PROCESSOR_RESPONSE_JSON, //写处理器-响应JSON
    WRITE_PROCESSOR_RESPONSE_HTML, //写处理器-响应HTML
    WRITE_PROCESSOR_RESPONSE_HEADER, //写处理器-响应HEADER
}
