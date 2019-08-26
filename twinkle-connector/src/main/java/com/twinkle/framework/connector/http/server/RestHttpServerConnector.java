package com.twinkle.framework.connector.http.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.component.rule.IRule;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.connector.http.endpoint.HttpEndpoint;
import com.twinkle.framework.connector.server.AbstractServer;
import com.twinkle.framework.connector.server.ServerConnector;
import com.twinkle.framework.connector.http.server.classloader.RestControllerClassLoader;
import com.twinkle.framework.core.datastruct.descriptor.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-14 17:50<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class RestHttpServerConnector extends AbstractComponent implements ServerConnector {
    private final static String REST_CONTROLLER_PATH = "com.twinkle.framework.bootstarter.controller.";
    /**
     * Will be used as class name to build the connector class.
     */
    private String connectorName;
    private List<String> endpointNameList;
    private Map<String, HttpEndpoint> endpointMap;

    public RestHttpServerConnector() {
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.connectorName = _conf.getString("Name");
        JSONArray tempEndpointNameArray = _conf.getJSONArray("EndpointNames");
        JSONArray tempEndpointsArray = _conf.getJSONArray("Endpoints");
        if (CollectionUtils.isEmpty(tempEndpointNameArray) || CollectionUtils.isEmpty(tempEndpointsArray)) {
            throw new ConfigurationException(ExceptionCode.CONNECTOR_MADANTORY_ATTR_MISSED, "ConnectorManager.configure(): Connector's Endpoints is a mandatory parameter. ");
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

    @Override
    public void registerAsService() {
        ClassLoader currentLoader = this.getClass().getClassLoader();
        GeneralClassTypeDescriptor tempDescriptor = this.packConnectorDescriptor();
        RestControllerClassLoader tempLoader = new RestControllerClassLoader(currentLoader, tempDescriptor);
        try {
            Class<?> tempClass = tempLoader.loadClass(tempDescriptor.getClassName());
            log.debug("The class is : []" + tempClass);

            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(tempClass);
            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

            ComponentFactory.getInstance().registerBeanDefinition(StringUtils.uncapitalize(tempDescriptor.getName()), beanDefinition);
        } catch (ClassNotFoundException e) {
            log.error("Register connector failed.", e);
            throw new RuntimeException(e);
        }
    }

    private GeneralClassTypeDescriptor packConnectorDescriptor() {
        String tempShortClassName = this.getConnectorClassName();
        String tempClassName = REST_CONTROLLER_PATH + tempShortClassName;
        try {
            Class.forName(tempClassName);
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_DUPLICATE_CONNECTOR_NAME, "The connector with name [" + this.connectorName + "] exists already.");
        } catch (ClassNotFoundException e) {
            log.info("Did not find the connector[{}], so going to build one.", this.connectorName);
        }

        TypeDescriptor tempSuperDescriptor = new TypeDescriptorImpl(AbstractServer.class);

        GeneralClassTypeDescriptor tempDescriptor = new GeneralClassTypeDescriptorImpl(
                tempClassName,
                tempShortClassName,
                this.getClassDescription(tempClassName),
                this.getAttributeList(),
                tempSuperDescriptor,
                new HashSet<>(),
                this.getAnnotationList(),
                this.getMethodList()
        );
        return tempDescriptor;
    }

    /**
     * Get Description Name.
     *
     * @param _internalName
     * @return
     */
    private String getClassDescription(String _internalName){
        StringBuilder tempBuilder = new StringBuilder("L");
        String tempName = _internalName;
        tempName = tempName.replace(".", "/");
        tempBuilder.append(tempName);
        return tempBuilder.toString();
    }

    private String getConnectorClassName() {
        return StringUtils.capitalize(this.connectorName);
    }

    private Set<String> getAnnotationList() {
        Set<String> tempAnnotations = new HashSet<>(3);
        tempAnnotations.add("@org.springframework.web.bind.annotation.RestController");
        tempAnnotations.add("@lombok.extern.slf4j.Slf4j");
        tempAnnotations.add("@io.swagger.annotations.Api");
        return tempAnnotations;
    }

    /**
     * The Class's private parameters are moved to AbstractServer.
     *
     * @return
     */
    private List<AttributeDescriptor> getAttributeList() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Get Method List.
     *
     * @return
     */
    private List<MethodTypeDescriptor> getMethodList() {
        List<MethodTypeDescriptor> tempList = new ArrayList<>(this.endpointNameList.size());
        for (Map.Entry<String, HttpEndpoint> tempEntry : this.endpointMap.entrySet()) {
            tempList.add(tempEntry.getValue().getMethodDescriptor());
        }
        return tempList;
    }
}
