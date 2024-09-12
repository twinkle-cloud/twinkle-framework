package com.twinkle.framework.api.component.datacenter;

import com.twinkle.framework.api.component.IConfigurableComponent;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.DataCenterException;

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
public interface IStatementExecutor extends IConfigurableComponent {
    /**
     * Check the given statement is valid or not?
     *
     * @param _definedStatementList
     */
    void filterStatement(List<IStatement> _definedStatementList);

    /**
     * Execute the Sql Statement.
     *
     * @param _context
     * @throws DataCenterException
     */
    void execute(NormalizedContext _context) throws DataCenterException;
}
