package com.twinkle.framework.connector;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private static Map<String, Connector> connectorMap = null;

    public ConnectorManager() {
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        JSONArray tempArray = _conf.getJSONArray("Connectors");
        if (CollectionUtils.isEmpty(tempArray)) {
            throw new ConfigurationException(ExceptionCode.CONNECTOR_MADANTORY_ATTR_MISSED, "ConnectorManager.configure(): Connectors is a mandatory parameter. ");
        }
        try {
            //Build the connector one by one.
            for (int i = 0; i < tempArray.size(); i++) {
                JSONObject tempObj = tempArray.getJSONObject(i);
                String tempName = tempObj.getString("Name");
                String tempClassName = tempObj.getString("ClassName");
                Connector tempConnector = (Connector)((Class.forName(tempClassName)).newInstance());
                tempConnector.configure(tempObj);

                this.addConnector(tempName, tempConnector);
            }
        } catch (ClassNotFoundException ex) {
            log.error("ConnectorManager.configure(): Connector not found.", ex);
            throw new ConfigurationException(ExceptionCode.CONNECTOR_CLASS_MISSED, "ConnectorManager.configure(): Connector class not found.");
        } catch (InstantiationException ex) {
            log.error("ConnectorManager.configure(): Connector instantiated failed.", ex);
            throw new ConfigurationException(ExceptionCode.CONNECTOR_INSTANTIATED_FAILED, "ConnectorManager.configure(): Connector instantiated failed.");
        } catch (IllegalAccessException ex) {
            log.error("ConnectorManager.configure(): The access level of the connector class is incorrect.", ex);
            throw new ConfigurationException(ExceptionCode.CONNECTOR_ACCESS_INCORRECT, "ConnectorManager.configure(): The class access level of the connector is incorrect.");
        }
    }

    /**
     * Get the connector by name.
     *
     * @param _name
     * @return
     */
    public static Connector getConnector(String _name) {
        if(connectorMap == null) {
            connectorMap = new HashMap<>(8);
        }
        return connectorMap.get(_name);
    }

    /**
     * Add a connector into the manager center.
     *
     * @param _name
     * @param _connector
     */
    private void addConnector(String _name, Connector _connector) {
        if(connectorMap == null) {
            connectorMap = new HashMap<>(8);
        }
        connectorMap.put(_name, _connector);
    }

    /**
     * Remove the specified connector.
     *
     * @param _name
     */
    public static void removeConnector(String _name) {
        if(connectorMap == null) {
            connectorMap = new HashMap<>(8);
        }
        connectorMap.remove(_name);
    }

    /**
     * Get the Iterator for the connector list.
     *
     * @return
     */
    public static Iterator getConnectors() {
        if(connectorMap == null) {
            connectorMap = new HashMap<>(8);
        }
        return connectorMap.values().iterator();
    }
}
