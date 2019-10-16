package com.twinkle.framework.datasource.aop;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Function: Abstract Routing Data Source. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DynamicAspectJExpressionPointcut extends AspectJExpressionPointcut {
    private Map<String, String> matchesCache;

    private String dataSourceName;

    public DynamicAspectJExpressionPointcut(String _expression, String _dataSourceName,
                                            Map<String, String> matchesCache) {
        this.dataSourceName = _dataSourceName;
        this.matchesCache = matchesCache;
        setExpression(_expression);
    }

    @Override
    public boolean matches(Method _method, Class<?> _targetClass, boolean _introductions) {
        boolean matches = super.matches(_method, _targetClass, _introductions);
        if (matches) {
            this.matchesCache.put(_targetClass.getName() + "." + _method.getName(), this.dataSourceName);
        }
        return matches;
    }
}