package com.krt.common.annotation;

import java.lang.annotation.*;

/**
 * 在实体类中对字段进行注解，用于流程表单权限和分支条件设置
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ActField {
    /**
     * 显示名字
     *
     * @return
     */
    String name();

    /**
     * 是否是子表单
     */
    boolean isChildForm() default false;

    /**
     * 是否用于分支条件判断
     *
     * @return
     */
    boolean isJudg() default false;
}
