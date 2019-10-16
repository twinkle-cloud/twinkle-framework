package com.twinkle.framework.datasource.processor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class DataSourceProcessor {

    private DataSourceProcessor nextProcessor;

    public void setNextProcessor(DataSourceProcessor dataSourceProcessor) {
        this.nextProcessor = dataSourceProcessor;
    }

    /**
     * Do the match check.
     *
     * @param _key @TwinkleDataSource.value.
     * @return
     */
    public abstract boolean matches(String _key);

    /**
     * Get the datasource.
     * <pre>
     *     invoke doDetermineDatasource()，
     *     If null, then go ahead the next processor.
     * </pre>
     *
     * @param _invocation Method info.
     * @param _key        @TwinkleDataSource.value.
     * @return
     */
    public String determineDatasource(MethodInvocation _invocation, String _key) {
        if (this.matches(_key)) {
            String tempDataSourceName = doDetermineDatasource(_invocation, _key);
            if (tempDataSourceName == null && this.nextProcessor != null) {
                return this.nextProcessor.determineDatasource(_invocation, _key);
            }
            return tempDataSourceName;
        }
        if (this.nextProcessor != null) {
            return this.nextProcessor.determineDatasource(_invocation, _key);
        }
        return null;
    }

    /**
     * Get the matched data source name.
     *
     * @param _invocation method info
     * @param _key @TwinkleDataSource.value.
     * @return
     */
    public abstract String doDetermineDatasource(MethodInvocation _invocation, String _key);
}
