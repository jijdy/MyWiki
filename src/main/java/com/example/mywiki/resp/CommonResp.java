package com.example.mywiki.resp;

public class CommonResp<T> {
    //业务处理完成
    private boolean success = true;

    //返回失败信息
    private String message;

    //正常的返回信息内容
    private T content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuffer sb= new StringBuffer("ResponseDto{");
        sb.append("success = ").append(success);
        sb.append("message = ").append(message);
        sb.append("content :").append(content);
        sb.append("}");
        return sb.toString();
    }
}
