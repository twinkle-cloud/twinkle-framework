package com.twinkle.framework.api.component.datacenter;

import com.twinkle.framework.api.component.IComponentFactory;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/19/19 4:24 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IStatementExecutor extends IStatement {
    /**
     * Check the given statement is valid or not?
     *
     * @param _definedStatementList
     */
    void checkStatement(List<IComponentFactory.ComponentNamePair> _definedStatementList);

    /**
     * Type is no use for executor.
     *
     * @return
     */
    @Override
    default int getType() {
        return TYPE_NONE;
    }
}
