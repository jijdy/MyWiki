package com.example.mywiki.exception;

/*
* 自定义异常，继承RunTimeException来使用其功能
* */
public class BusinessException extends RuntimeException{

    private BusinessExceptionCode code;

    public BusinessException(BusinessExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public BusinessExceptionCode getCode() {
        return code;
    }

    public void setCode(BusinessExceptionCode code) {
        this.code = code;
    }

    //不写入堆栈学习，减小信息的输出
    @Override
    public Throwable fillInStackTrace() {

        return this;
    }
}