package com.twinkle.framework.api.component.datacenter;

import com.twinkle.framework.api.component.IComponent;
import com.twinkle.framework.api.config.Configurable;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/19/19 5:54 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IDataCenterManager extends Configurable, IComponent {
    /**
     * Get the statement executor with name.
     *
     * @param _executorName
     * @return
     */
    IStatementExecutor getStatementExecutor(String _executorName);
}
