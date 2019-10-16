package com.twinkle.framework.datasource.aop;

import com.twinkle.framework.datasource.DynamicDataSourceClassResolver;
import com.twinkle.framework.datasource.annotation.TwinkleDataSource;
import com.twinkle.framework.datasource.processor.DataSourceProcessor;
import com.twinkle.framework.datasource.utils.DynamicDataSourceContextHolder;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Function: Core Interceptor of Dynamic Datasource. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {
    /**
     * The identification of SPEL.
     */
    private static final String DYNAMIC_PREFIX = "#";
    private static final DynamicDataSourceClassResolver RESOLVER = new DynamicDataSourceClassResolver();
    @Setter
    private DataSourceProcessor dataSourceProcessor;

    @Override
    public Object invoke(MethodInvocation _invocation) throws Throwable {
        try {
            DynamicDataSourceContextHolder.push(determineDatasource(_invocation));
            return _invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }

    /**
     * Determine the data source according to the invocation method.
     *
     * @param _invocation
     * @return
     * @throws Throwable
     */
    private String determineDatasource(MethodInvocation _invocation) throws Throwable {
        Method tempMethod = _invocation.getMethod();
        TwinkleDataSource tempDataSourceAnnotation = tempMethod.isAnnotationPresent(TwinkleDataSource.class)
                ? tempMethod.getAnnotation(TwinkleDataSource.class)
                : AnnotationUtils.findAnnotation(RESOLVER.targetClass(_invocation), TwinkleDataSource.class);
        String tempKey = tempDataSourceAnnotation.value();
        return (!tempKey.isEmpty() && tempKey.startsWith(DYNAMIC_PREFIX)) ? this.dataSourceProcessor
                .determineDatasource(_invocation, tempKey) : tempKey;
    }
}