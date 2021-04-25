package org.alex.platform.core.common;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.enums.RelyType;
import org.alex.platform.util.TimeUtil;

public interface Node {
    /**
     * 封装链路跟踪节点对象
     * @param type 类型枚举
     * @param id 对应其数据表中存储的编号 用例对应日志表；依赖对应两张依赖表，其他为null
     * @param name 名称
     * @param value 当时的value
     * @param runTime 执行耗时
     * @param expression 提取表达式
     * @return json对象
     */
    default JSONObject chainNode(RelyType type, Integer id, String name, String value, long runTime, String expression) {
        JSONObject object = new JSONObject();
        String typeStr;
        String desc;
        if (type == RelyType.CASE_START) {
            typeStr = "执行用例";
            desc = "开始执行";
        } else if (type == RelyType.CASE_END) {
            typeStr = "执行用例";
            desc = "执行完成";
        } else if (type == RelyType.RELY_START) {
            typeStr = "接口依赖";
            desc = "开始执行";
        } else if (type == RelyType.RELY_END) {
            typeStr = "接口依赖";
            desc = "执行完成";
        } else if (type == RelyType.INVOKE) {
            typeStr = "反射方法";
            desc = "反射方法";
        } else if (type == RelyType.PRE_CASE_START){
            typeStr = "前置用例";
            desc = "开始执行";
        } else if (type == RelyType.PRE_CASE_END){
            typeStr = "前置用例";
            desc = "执行完成";
        } else if (type == RelyType.INTERFACE_JSON){
            typeStr = "接口依赖";
            desc = "Json";
        } else if (type == RelyType.INTERFACE_HTML){
            typeStr = "接口依赖";
            desc = "Html";
        } else if (type == RelyType.INTERFACE_HEADER){
            typeStr = "接口依赖";
            desc = "Header";
        } else if (type == RelyType.SQL_SELECT){
            typeStr = "SQL依赖";
            desc = "查询语句";
        } else if (type == RelyType.SQL_DELETE){
            typeStr = "SQL依赖";
            desc = "删除语句";
        } else if (type == RelyType.SQL_INSERT){
            typeStr = "SQL依赖";
            desc = "新增语句";
        } else if (type == RelyType.SQL_UPDATE){
            typeStr = "SQL依赖";
            desc = "修改语句";
        } else if (type == RelyType.SQL_SCRIPT){
            typeStr = "SQL依赖";
            desc = "脚本处理";
        } else if (type == RelyType.CONST){
            typeStr = "固定字符";
            desc = "固定字符";
        } else if (type == RelyType.READ_PROCESSOR){
            typeStr = "使用缓存";
            desc = "使用缓存";
        } else if (type == RelyType.WRITE_PROCESSOR_REQUEST_HEADER){
            typeStr = "参数缓存";
            desc = "Header";
        } else if (type == RelyType.WRITE_PROCESSOR_REQUEST_PARAMS){
            typeStr = "参数缓存";
            desc = "Params";
        } else if (type == RelyType.WRITE_PROCESSOR_REQUEST_DATA){
            typeStr = "参数缓存";
            desc = "Data";
        } else if (type == RelyType.WRITE_PROCESSOR_REQUEST_JSON){
            typeStr = "参数缓存";
            desc = "Json";
        } else if (type == RelyType.WRITE_PROCESSOR_RESPONSE_JSON){
            typeStr = "响应缓存";
            desc = "Json";
        } else if (type == RelyType.WRITE_PROCESSOR_RESPONSE_HTML){
            typeStr = "响应缓存";
            desc = "Html";
        } else if (type == RelyType.WRITE_PROCESSOR_RESPONSE_HEADER){
            typeStr = "响应缓存";
            desc = "Header";
        } else {
            typeStr = "其它";
            desc = "其它";
        }
        object.put("type", type);
        object.put("typeDesc", typeStr);
        object.put("id", id);
        object.put("name", name);
        object.put("value", value);
        object.put("time", runTime);
        object.put("desc", desc);
        object.put("expression", expression);
        object.put("date", TimeUtil.date("yyyy-MM-dd HH:mm:ss:SSS"));
        return object;
    }
}
