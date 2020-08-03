package org.alex.platform.controller;

import org.alex.platform.common.Result;
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

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result globalException(Exception e) {
        LOG.error(e.getMessage());
        // validate抛出的异常
        if (e instanceof BindException) {
            BindException be = (BindException) e;
            List<ObjectError> errors = be.getBindingResult().getAllErrors();
            String msg = errors.get(0).getDefaultMessage();
            return Result.fail(405, msg);
        } else {
            return Result.fail(e.getMessage());
        }
    }
}
