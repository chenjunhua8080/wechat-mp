
package com.cjh.wechatmp.exception;

import com.cjh.wechatmp.enums.ErrorEnum;
import com.cjh.wechatmp.response.Result;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerCenter {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleRRException(ServiceException e) {
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Result.failure(ErrorEnum.FAIL.getCode(), "数据库中已存在该记录");
    }

    @ExceptionHandler(NullPointerException.class)
    public Result handleNullException(NullPointerException e) {
        log.error(e.getMessage(), e);
        return Result.failure("为空异常");
    }

    @ExceptionHandler(BindException.class)
    public Result handleException(BindException e) {
        // 获得的类型为typeMismatch.java.lang.Long，需要去掉typeMismatch.前缀
        String fieldType = e.getFieldError().getCodes()[2].substring(e.getFieldError().getCodes()[3].length() + 1);
        if (!"typeMismatch".equals(e.getFieldError().getCodes()[3])) {
            // 获取注解错误信息
            return Result.failure(ErrorEnum.FAIL.getCode(), e.getFieldError().getDefaultMessage());
        }
        log.error(
            "参数类型不正确,参数名:" + e.getFieldError().getField() + ",参数值:" + e.getFieldError().getRejectedValue()
                + ",类型应该为:"
                + fieldType);
        return Result.failure(ErrorEnum.FAIL.getCode(), "参数类型不正确");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        log.error("参数不正确,参数名:" + fieldErrors.get(0).getField() + ",错误结果:" + fieldErrors.get(0).getDefaultMessage());
        return Result.failure(ErrorEnum.FAIL.getCode(), fieldErrors.get(0).getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleException(HttpMessageNotReadableException e) {
        return Result.failure(ErrorEnum.FAIL.getCode(), "参数类型不正确:" + e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleException(MissingServletRequestParameterException e) {
        return Result.failure(ErrorEnum.FAIL.getCode(), "缺少参数:" + e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleException(HttpRequestMethodNotSupportedException e) {
        return Result.failure("请求方式不正确:" + e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e) {
        log.error(e.getMessage(), e);
        if (e instanceof ConstraintViolationException) {
            return Result.failure(ErrorEnum.FAIL.getCode(), "参数异常：" + e.getMessage());
        } else if (e instanceof SQLSyntaxErrorException) {
            return Result.failure("sql异常：" + e.getMessage());
        } else {
            return Result.failure();
        }
    }

}
