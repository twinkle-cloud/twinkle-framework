package com.twinkle.framework.connector;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractConfigurableComponent;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.connector.server.ServerConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

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
public class ConnectorManager extends AbstractConfigurableComponent implements Configurable {
    /**
     * Connector name list.
     */
    private List<String> connectorNameList;
    /**
     * Connector Map.
     */
    private Map<String, Connector> connectorMap = null;

    public ConnectorManager() {
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
        JSONArray tempNameArray = _conf.getJSONArray("ConnectorNames");
        JSONArray tempConnectorArray = _conf.getJSONArray("Connectors");
        if (CollectionUtils.isEmpty(tempNameArray) || CollectionUtils.isEmpty(tempConnectorArray)) {
            throw new ConfigurationException(ExceptionCode.CONNECTOR_MANDATORY_ATTR_MISSED, "ConnectorManager.configure(): Connectors is a mandatory parameter. ");
        }
        this.connectorNameList = new ArrayList<>(tempNameArray.size());
        this.connectorMap = new HashMap<>(tempNameArray.size());

        //Build the connector one by one.
        for (int i = 0; i < tempNameArray.size(); i++) {
            String tempItem = tempNameArray.getString(i);
            for (int j = 0; j < tempConnectorArray.size(); j++) {
                JSONObject tempObj = tempConnectorArray.getJSONObject(j);
                if (tempObj.getString("Name").equals(tempItem)) {
                    Connector tempConnector = ComponentFactory.getInstance().loadComponent(this.getFullPathName(), tempObj);
                    this.connectorNameList.add(tempItem);
                    this.addConnector(tempItem, tempConnector);
                    break;
                }
            }
        }
    }

    /**
     * Get the connector by name.
     *
     * @param _name
     * @return
     */
    public Connector getConnector(String _name) {
        return connectorMap.get(_name);
    }

    /**
     * Add a connector into the manager center.
     *
     * @param _name
     * @param _connector
     */
    private void addConnector(String _name, Connector _connector) {
        if (_connector instanceof ServerConnector) {
            ((ServerConnector) _connector).registerAsService();
        }
        connectorMap.put(_name, _connector);
    }

    /**
     * Remove the specified connector.
     *
     * @param _name
     */
    public void removeConnector(String _name) {
        connectorMap.remove(_name);
    }

    /**
     * Get the Iterator for the connector list.
     *
     * @return
     */
    public Iterator getConnectors() {
        return connectorMap.values().iterator();
    }
}
