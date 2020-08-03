package org.alex.platform.common;

import java.util.HashMap;

public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    public static Result success() {
        return new Result(200, "操作成功", new HashMap<>());
    }

    public static Result success(String msg) {
        return new Result(200, msg, new HashMap<>());
    }

    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result fail() {
        return new Result(500, "操作失败", new HashMap<>());
    }

    public static Result fail(String msg) {
        return new Result(500, msg, new HashMap<>());
    }

    public static Result fail(int code, String msg) {
        return new Result(code, msg, new HashMap<>());
    }

}
