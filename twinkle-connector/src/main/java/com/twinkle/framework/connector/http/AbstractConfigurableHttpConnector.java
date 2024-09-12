package com.twinkle.framework.connector.http;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractConfigurableComponent;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.connector.Connector;
import com.twinkle.framework.connector.http.endpoint.HttpEndpoint;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2024-07-14 17:40<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 21
 */
public class AbstractConfigurableHttpConnector extends AbstractConfigurableComponent implements Connector {
    /**
     * Will be used as class name to build the connector class.
     */
    private String connectorName;
    private List<String> endpointNameList;
    private Map<String, HttpEndpoint> endpointMap;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.connectorName = _conf.getString("Name");
        JSONArray tempEndpointNameArray = _conf.getJSONArray("EndpointNames");
        JSONArray tempEndpointsArray = _conf.getJSONArray("Endpoints");
        if (CollectionUtils.isEmpty(tempEndpointNameArray) || CollectionUtils.isEmpty(tempEndpointsArray)) {
            throw new ConfigurationException(ExceptionCode.CONNECTOR_MANDATORY_ATTR_MISSED, "ConnectorManager.configure(): Connector's Endpoints is a mandatory parameter. ");
        }
        this.endpointNameList = new ArrayList<>(tempEndpointNameArray.size());
        this.endpointMap = new HashMap<>(tempEndpointNameArray.size());
        for (int i = 0; i < tempEndpointNameArray.size(); i++) {
            String tempItem = tempEndpointNameArray.getString(i);
            for(int j =0; j<tempEndpointsArray.size(); j++) {
                JSONObject tempObj = tempEndpointsArray.getJSONObject(j);
                if(tempObj.getString("Name").equals(tempItem)) {
                    StringBuilder tempBuilder = new StringBuilder(this.getFullPathName());
                    tempBuilder.append((char) 92);
                    tempBuilder.append(tempItem);
                    HttpEndpoint tempEndPoint = ComponentFactory.getInstance().loadComponent(tempBuilder.toString(), tempObj);

                    this.endpointMap.put(tempItem, tempEndPoint);
                    break;
                }
            }
        }
    }
}
