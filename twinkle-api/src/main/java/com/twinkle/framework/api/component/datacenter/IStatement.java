package com.twinkle.framework.api.component.datacenter;

import com.twinkle.framework.api.component.IComponent;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.DataCenterException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/16/19 6:32 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IStatement extends IComponent, Configurable {
    int TYPE_SQL = 0;
    int TYPE_MONGODB = 1;
    int TYPE_GRAPH = 2;
    int TYPE_LUCENE = 3;
    int TYPE_ELASTIC = 4;
    int TYPE_NONE = 10;

    /**
     * Statements path.
     */
    String STATEMENT_PATH = "DataCenterManager/Statements";

    /**
     * Execute the Sql Statement.
     *
     * @param _context
     * @param _dataSourceName
     * @throws DataCenterException
     */
    void execute(NormalizedContext _context, String _dataSourceName) throws DataCenterException;

    /**
     * Get the statement type.
     * Refer to: TYPE_xxx.
     * @return
     */
    int getType();
}
