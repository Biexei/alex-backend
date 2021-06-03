package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.LoginException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
        LOG.warn(ExceptionUtil.msg(e));
        if (e instanceof BusinessException || e instanceof SqlException) {
            LOG.warn(ExceptionUtil.msg(e));
            return Result.fail(501, e.getMessage());
        } else if (e instanceof DataAccessException) {
            LOG.error("{}", e.getMessage(), e);
            return Result.fail(504, "数据库异常");
        } else if (e instanceof MethodArgumentNotValidException) { // json请求验证-requestBody
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            List<ObjectError> errors = bindingResult.getAllErrors();
            String msg = errors.get(0).getDefaultMessage();
            LOG.warn(msg);
            if (msg != null) {
                return Result.fail(501, msg);
            } else {
                return Result.fail(501, "服务异常");
            }
        } else if (e instanceof BindException) { // 表单请求验证
            BindException be = (BindException) e;
            List<ObjectError> errors = be.getBindingResult().getAllErrors();
            String msg = errors.get(0).getDefaultMessage();
            LOG.warn(msg);
            if (msg != null) {
                if (msg.startsWith("Failed to convert property value of type ")) {
                    return Result.fail(501, "参数类型错误");
                } else {
                    return Result.fail(501, msg);
                }
            }
            return Result.fail(501, "服务异常");
        } else if (e instanceof LoginException) {
            LOG.warn(ExceptionUtil.msg(e));
            return Result.fail(400, e.getMessage());
        } else {
            LOG.error("{}", e.getMessage(), e);
            return Result.fail(500, "服务异常");
        }
    }
}
