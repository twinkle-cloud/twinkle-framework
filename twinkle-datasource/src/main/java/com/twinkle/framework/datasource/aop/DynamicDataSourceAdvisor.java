package com.twinkle.framework.datasource.aop;

import com.twinkle.framework.datasource.matcher.ExpressionMatcher;
import com.twinkle.framework.datasource.matcher.Matcher;
import com.twinkle.framework.datasource.matcher.RegexMatcher;
import com.twinkle.framework.datasource.processor.DataSourceProcessor;
import com.twinkle.framework.datasource.utils.DynamicDataSourceContextHolder;
import lombok.Setter;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function: Data Source Advisor. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DynamicDataSourceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    /**
     * The identification of SPEL
     */
    private static final String DYNAMIC_PREFIX = "#";

    @Setter
    private DataSourceProcessor dataSourceProcessor;

    private Advice advice;

    private Pointcut pointcut;

    private Map<String, String> matchesCache = new HashMap<String, String>();

    public DynamicDataSourceAdvisor(List<Matcher> _matchers) {
        this.pointcut = buildPointcut(_matchers);
        this.advice = buildAdvice();
    }

    private Advice buildAdvice() {
        return new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation _invocation) throws Throwable {
                try {
                    Method tempMethod = _invocation.getMethod();
                    String tempMethodPath = _invocation.getThis().getClass().getName() + "." + tempMethod.getName();
                    String tempKey = matchesCache.get(tempMethodPath);
                    if (tempKey != null && !tempKey.isEmpty() && tempKey.startsWith(DYNAMIC_PREFIX)) {
                        tempKey = dataSourceProcessor.determineDatasource(_invocation, tempKey);
                    }
                    DynamicDataSourceContextHolder.push(tempKey);
                    return _invocation.proceed();
                } finally {
                    DynamicDataSourceContextHolder.poll();
                }
            }
        };
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory _factory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(_factory);
        }
    }

    private Pointcut buildPointcut(List<Matcher> _matchers) {
        ComposablePointcut tempComposablePointcut = null;
        for (Matcher tempMatcher : _matchers) {
            if (tempMatcher instanceof RegexMatcher) {
                RegexMatcher tempRegexMatcher = (RegexMatcher) tempMatcher;
                Pointcut tempPointCut = new DynamicJdkRegexpMethodPointcut(tempRegexMatcher.getPattern(),
                        tempRegexMatcher.getDataSource(), this.matchesCache);
                if (tempComposablePointcut == null) {
                    tempComposablePointcut = new ComposablePointcut(tempPointCut);
                } else {
                    tempComposablePointcut.union(tempPointCut);
                }
            } else {
                ExpressionMatcher tempExpressionMatcher = (ExpressionMatcher) tempMatcher;
                Pointcut tempPointCut = new DynamicAspectJExpressionPointcut(tempExpressionMatcher.getExpression(),
                        tempExpressionMatcher.getDataSource(), matchesCache);
                if (tempComposablePointcut == null) {
                    tempComposablePointcut = new ComposablePointcut(tempPointCut);
                } else {
                    tempComposablePointcut.union(tempPointCut);
                }
            }
        }
        return tempComposablePointcut;
    }
}
