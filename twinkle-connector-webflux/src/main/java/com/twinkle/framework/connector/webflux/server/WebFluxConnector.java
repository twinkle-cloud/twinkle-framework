package com.twinkle.framework.connector.webflux.server;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractConfigurableComponent;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.connector.http.endpoint.HttpEndpoint;
import com.twinkle.framework.connector.server.ServerConnector;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebFluxConnector extends AbstractConfigurableComponent implements ServerConnector {
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
        RouterFunctions.Builder tempBuilder = RouterFunctions.route();
        for (int i = 0; i < tempEndpointNameArray.size(); i++) {
            String tempItem = tempEndpointNameArray.getString(i);
            for(int j =0; j<tempEndpointsArray.size(); j++) {
                JSONObject tempObj = tempEndpointsArray.getJSONObject(j);
                if(tempObj.getString("Name").equals(tempItem)) {

                    break;
                }
            }
        }
    }

    @Override
    public void registerAsService() {

    }

    private void addRoute(RouterFunctions.Builder _builder, JSONObject _endPoint) {
        String tempURL = _endPoint.getString("URL");
        RequestMethod tempRequestMethod = RequestMethod.valueOf(_endPoint.getString("RequestType"));

        RouterFunctions.route()
                .GET("/hello/{name}", serverRequest -> {
                    String name = serverRequest.pathVariable("name");
                    return ServerResponse.ok().bodyValue(name);
                }).build();
        switch (tempRequestMethod) {
            case GET:
                _builder.route(RequestPredicates.GET(tempURL,));
                return;
            case PUT:
                return "update" + tempName;
            case POST:
                return "post" + tempName;
            case DELETE:
                return "delete" + tempName;
            default:
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "Http Method[" + this.requestType + "] is not supported currently.");
        }
    }
}
