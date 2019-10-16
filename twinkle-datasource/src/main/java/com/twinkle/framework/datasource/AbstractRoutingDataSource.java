package com.twinkle.framework.datasource;

import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Function: Abstract Routing Data Source. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractRoutingDataSource extends AbstractDataSource {
    /**
     * Determine witch data source will be return.
     *
     * @return
     */
    protected abstract DataSource determineDataSource();

    @Override
    public Connection getConnection() throws SQLException {
        return determineDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String _userName, String _password) throws SQLException {
        return determineDataSource().getConnection(_userName, _password);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> _iface) throws SQLException {
        if (_iface.isInstance(this)) {
            return (T) this;
        }
        return determineDataSource().unwrap(_iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> _iface) throws SQLException {
        return (_iface.isInstance(this) || determineDataSource().isWrapperFor(_iface));
    }
}
