package com.example.mywiki.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserResetPasswordReq {

    private Long id;

    @NotNull(message = "请设置用户密码！")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$" , message = "【密码】至少包含数字和字母，且长度为6·32")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserSaveReq{" +
                "id=" + id +
                ", password='" + password + '\'' +
                '}';
    }
}