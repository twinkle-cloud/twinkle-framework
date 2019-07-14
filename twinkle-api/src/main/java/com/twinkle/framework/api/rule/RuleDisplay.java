package com.twinkle.framework.api.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RuleDisplay Rule进行页面编辑时，展示的信息 <br/>
 * Date:    2019年7月14日 下午7:44:58 <br/>
 *
 * @author yukang
 * @see
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RuleDisplay {

    /**
     * rule 的名字
     * */
    String name();

    /**
     * rule 的描述
     * */
    String description();

}