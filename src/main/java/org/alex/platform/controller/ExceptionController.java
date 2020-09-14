package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
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
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result globalException(Exception e) {
        LOG.error(e.getMessage());
        // validate抛出的异常
        if (e instanceof BindException) {
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
