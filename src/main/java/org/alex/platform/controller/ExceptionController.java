package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@ControllerAdvice
public class ExceptionController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);

    /**
     * 自定义全局异常处理器
     *
     * @param e e
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result globalException(Exception e) {
        e.printStackTrace();
        LOG.error(e.getMessage());
        if (e instanceof DataAccessException) {
            return Result.fail(504, "数据库异常");
        } else if (e instanceof MethodArgumentNotValidException) { // json请求验证-requestBody
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            List<ObjectError> errors = bindingResult.getAllErrors();
            String msg = errors.get(0).getDefaultMessage();
            return Result.fail(501, msg);
        } else if (e instanceof ResourceAccessException) {
            return Result.fail(502, "代理服务器未开启" + e.getMessage());
        } else if (e instanceof BindException) { // 表单请求验证
            BindException be = (BindException) e;
            List<ObjectError> errors = be.getBindingResult().getAllErrors();
            String msg = errors.get(0).getDefaultMessage();
            return Result.fail(501, msg);
        } else if (e instanceof BusinessException) {
            return Result.fail(502, e.getMessage());
        } else if (e instanceof ParseException) {
            return Result.fail(503, e.getMessage());
        } else if (e instanceof SqlException) {
            return Result.fail(504, e.getMessage());
        } else {
            return Result.fail(500, e.getMessage());
        }
    }
}
