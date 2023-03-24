package com.powernode.annotations;


import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/*
*   权限注释：自定义注解 ，和 权限标识符 进行绑定
* */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AppPermission {

    /*
    *   配置权限标识符  如：system:user:add
    * */
    String value();

}
