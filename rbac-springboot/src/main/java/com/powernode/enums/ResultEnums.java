package com.powernode.enums;

import lombok.Getter;

/**
 *     返回的结果信息 玫举类
 */
@Getter
public enum ResultEnums {
    /**
     * 除20000外，其余都是失败，每个返回码代表具体失败场景
     */
    SUCCESS(20000, "操作成功"),


    ERROR(40000, "操作失败！"),
    // 用户未登录
    USER_NOT_FOUND(40001, "账户不在在"),

    USER_PASSWORD_ERROR(40002, "账户或者密码错误"),

    USER_DISABLED_ERROR(40003, "账户被禁用"),

    USER_LOCKED_ERROR(40004, "账户被锁定"),

    USER_NOT_LOGIN(40005, "用户未登录"),

    USER_NOT_ALLOW_ACCESS(40006, "会话已过期，请重新登陆"),

    PARAMS_NULL(40007, "参数不能为空！"),

    PARAMS_ERROR(40008, "参数不合法！"),

    MENU_EXISTS(40009, "菜单已存在"),

    USER_EXISTS(40010, "账号已存在");

    private Integer code;
    private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}