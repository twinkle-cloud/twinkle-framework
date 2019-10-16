package com.twinkle.framework.datasource.aop;

import com.twinkle.framework.datasource.annotation.TwinkleDataSource;
import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Function: Data Source Advisor. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {
    private Advice advice;

    private Pointcut pointcut;

    public DynamicDataSourceAnnotationAdvisor(
            @NonNull DynamicDataSourceAnnotationInterceptor dynamicDataSourceAnnotationInterceptor) {
        this.advice = dynamicDataSourceAnnotationInterceptor;
        this.pointcut = buildPointcut();
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
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    /**
     * Build the Point Cut.
     *
     * @return
     */
    private Pointcut buildPointcut() {
        Pointcut tempAnnotationPointcut = new AnnotationMatchingPointcut(TwinkleDataSource.class, true);
        Pointcut tempMethodPointCut = AnnotationMatchingPointcut.forMethodAnnotation(TwinkleDataSource.class);
        return new ComposablePointcut(tempAnnotationPointcut).union(tempMethodPointCut);
    }
}
