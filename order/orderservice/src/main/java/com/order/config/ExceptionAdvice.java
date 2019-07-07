package com.order.config;

import com.order.utils.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionAdvice {

    /**
     * 处理controller入参参数校验异常，需要在入参bean上加@Valid或者@Validated。同时在bean属性上加验证注解标签。
     *
     * @param bindException
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public @ResponseBody
    ResponseData errorHandler(BindException bindException) {
        Map<String, String> map = new HashMap<>();
        BindingResult bindingResult = bindException.getBindingResult();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        return new ResponseData<>(HttpStatus.CONTINUE.value(), "异常原因：" + map);
    }

}

