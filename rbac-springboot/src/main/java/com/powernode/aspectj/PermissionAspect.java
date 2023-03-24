package com.powernode.aspectj;


import cn.hutool.core.collection.CollectionUtil;
import com.powernode.annotations.AppPermission;
import com.powernode.commen.Result;
import com.powernode.constants.CoreConstant;
import com.powernode.enums.ResultEnums;
import com.powernode.enums.StateEnums;
import com.powernode.exceptions.BusinessException;
import com.powernode.utils.ServletUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

/*
*   切面类   切入点+增强
* */

@Component
@Aspect
@EnableAspectJAutoProxy    // 开启Aspect
public class PermissionAspect {

    // 前置增强，拦截标注有 @AppPermission注解 的所有资源(类或方法)
    @Before(value = "@annotation(appPermission)")    // 切入点，即拦截什么方法
    public void before(AppPermission appPermission){   // 增强
        // 1.获取 被拦截的方法 的 权限标识符   如：system:user:add 或 system:user:query ...
        String permission = appPermission.value();
        /*System.out.println(permission);*/
        // 2.从会话中获取 当前用户的所有 权限信息         CoreConstant.PERMISSIONS_KEY = "permission"
        HttpSession session = ServletUtils.getSession();
        List<String> permissionList = (List<String>) session.getAttribute(CoreConstant.PERMISSIONS_KEY);
        // 3.遍历 当前用户 所拥有的的权限
        // 如果当前用户没有任何权限 或者 所拥有的权限中不包含 被拦截的方法的权限标识符
        if (CollectionUtil.isEmpty(permissionList) || !permissionList.contains(permission)) {
            // 抛出异常，给到 全局异常处理类 进行处理    CoreConstant.NO_PERMISSION = "用户没有权限"
            throw new BusinessException(CoreConstant.NO_PERMISSION);
        }
        // 如果有，放行
    }

}
