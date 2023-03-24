package com.powernode.enums;

import lombok.Getter;

/**
 * 状态码枚举。所有的状态码都在这里编写
 */
@Getter
public enum StateEnums {
    /**
     * 逻辑删除状态
     */
    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除"),

    /**
     * 启用状态
     */
    ENABLED(0, "启用"),
    NOT_ENABLE(1, "停用"),

    /**
     * 用户类型
     */
    USER_TYPE_ADMIN(0, "超级管理员"),
    USER_TYPE_NORMAL(1, "系统用户"),

    /**
     * 性别状态
     */
    SEX_MAN(0, "男"),
    SEX_WOMAN(1, "女"),
    SEX_UN_KNOW(2, "未知"),

    /**
     * 请求访问状态枚举
     */
    REQUEST_SUCCESS(0, "请求正常"),
    REQUEST_ERROR(1, "请求异常"),

    /**
     * 菜单状态枚举
     */
    MENU_M(0, "目录"),
    MENU_C(1, "菜单"),
    MENU_F(2, "权限"),

    /**
     * 登陆状态
     */
    LOGIN_SUCCESS(0, "登陆成功"),
    LOGIN_ERROR(1, "登陆失败"),

    /**
     * 账号状态
     */
    DISABLED(1, "禁用"),
    NOT_DISABLED(0, "未禁用");


    private Integer code;
    private String msg;

    StateEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
