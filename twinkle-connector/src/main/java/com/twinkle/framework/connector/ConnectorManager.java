package com.twinkle.framework.connector;

import com.twinkle.framework.api.config.ConfigNode;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-14 17:42<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class ConnectorManager implements Configurable {
    /**
     * Connector Map.
     */
    private HashMap connectorMap = null;

    public ConnectorManager() {
        this.connectorMap = new HashMap();
    }

    @Override
    public void configure(ConfigNode _conf) throws ConfigurationException {

    }

    /**
     * Get the connector by name.
     *
     * @param _name
     * @return
     */
    public Connector getConnector(String _name) {
        return (Connector)this.connectorMap.get(_name);
    }

    /**
     * Add a connector into the manager center.
     *
     * @param _name
     * @param _connector
     */
    public void addConnector(String _name, Connector _connector) {
        this.connectorMap.put(_name, _connector);
    }

    /**
     * Remove the specified connector.
     *
     * @param _name
     */
    public void removeConnector(String _name) {
        this.connectorMap.remove(_name);
    }

    /**
     * Get the Iterator for the connector list.
     *
     * @return
     */
    public Iterator getConnectors() {
        return this.connectorMap.values().iterator();
    }
}
