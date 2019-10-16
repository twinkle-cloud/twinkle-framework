package com.twinkle.framework.datasource.processor;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class SpelExpressionDataSourceProcessor extends DataSourceProcessor {
    /**
     * Parameter discover.
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /**
     * Express parser.
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Override
    public boolean matches(String _key) {
        return true;
    }

    @Override
    public String doDetermineDatasource(MethodInvocation _invocation, String _key) {
        Method tempMethod = _invocation.getMethod();
        Object[] tempArguments = _invocation.getArguments();
        EvaluationContext tempContext = new MethodBasedEvaluationContext(null, tempMethod, tempArguments,
                NAME_DISCOVERER);
        Object tempKeyObj = PARSER.parseExpression(_key).getValue(tempContext);
        return tempKeyObj == null ? "" : tempKeyObj.toString();
    }
}
