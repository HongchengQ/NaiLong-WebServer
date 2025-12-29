package com.nailong.websdk.advice;

import com.nailong.websdk.exception.AuthorizationHeadException;
import com.nailong.websdk.exception.BadRequestException;
import com.nailong.websdk.exception.CommonException;
import com.nailong.websdk.domain.HttpRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.exc.ValueInstantiationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求 body 异常 -> {}", e.getMessage());
        return processResponse(new BadRequestException("无法解析请求消息内容"));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("访问一个不存在的地址或资源 -> {}", e.getMessage());
        return processResponse(new BadRequestException("试图访问一个不存在的地址或资源"));
    }

    @ExceptionHandler(DatabindException.class)
    public Object handleDatabindException(DatabindException e) {
        log.error("json 解析异常 -> {}", e.getMessage());
        return processResponse(new CommonException(e.getOriginalMessage(), 100400));
    }

    @ExceptionHandler(AuthorizationHeadException.class)
    public Object handleAuthorizationHeadException(AuthorizationHeadException e) {
        log.error("认证请求头异常 -> {}", e.getMessage());
        return processResponse(e);
    }

    @ExceptionHandler(CommonException.class)
    public Object handleBadRequestException(CommonException e) {
        log.error("自定义异常 -> {} , 异常原因：{}  ", e.getClass().getName(), e.getMessage());
        log.debug("", e);
        return processResponse(e);
    }

    @ExceptionHandler(ValueInstantiationException.class)
    public Object handleValueInstantiationException(ValueInstantiationException e) {
        log.error("实例解析异常 -> {}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("实例解析异常"));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public Object handleFileNotFoundException(FileNotFoundException e) {
        log.error("文件未找到异常 -> {}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("文件未找到异常"));
    }

    @ExceptionHandler(IOException.class)
    public Object handleIOException(IOException e) {
        log.error("IO 异常 -> {}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("IO 异常"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数校验异常 -> {}", msg);
        log.debug("", e);
        return processResponse(new BadRequestException(msg));
    }

    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException e) {
        log.error("请求参数绑定异常 ->BindException， {}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("请求参数格式错误"));
    }

    @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
    public Object handleMethodArgumentConversionNotSupportedException(MethodArgumentConversionNotSupportedException e) {
        log.error("请求方法参数转换不支持异常 -> MethodArgumentConversionNotSupportedException，{}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("请求方法参数处理异常"));
    }

    @ExceptionHandler(Exception.class)
    public Object handleRuntimeException(Exception e) {
        log.error("其他异常 : ", e);
        return processResponse(new CommonException("服务器内部异常", 500));
    }

    private ResponseEntity<Object> processResponse(CommonException e) {
        // 大于三位数时：代表需要把状态码转交到 body，所以协议层就得是200
        int statusCode = (e.getCode() > 999) ? 200 : e.getCode();

        return ResponseEntity
                .status(statusCode)
                .body(HttpRsp.error(e));
    }
}