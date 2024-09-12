package com.twinkle.framework.configure.component;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.IComponentFactory;
import com.twinkle.framework.api.component.IConfigurableComponent;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class AbstractComponentFactory implements IComponentFactory {
    protected Map<String, AtomicInteger> protoTypeIndexMap = new ConcurrentHashMap<>(8);
    @Override
    public <T extends IConfigurableComponent> T loadComponent(JSONObject _obj) {
        return this.loadComponent(null, _obj);
    }

    @Override
    public <T extends IConfigurableComponent> T loadGeneralComponent(JSONObject _obj) {
        if (_obj.isEmpty()) {
            return null;
        }
        String tempClassName = _obj.getString(KEY_CLASS_NAME);
        if (StringUtils.isBlank(tempClassName)) {
            throw new ConfigurationException(ExceptionCode.COMPONENT_CLASS_MISSED, "The [" + _obj + "]component class is empty");
        }
        try {
            ClassLoader tempClassLoader = this.getClass().getClassLoader();
            Class<?> tempClass = tempClassLoader.loadClass(tempClassName);
            if (null != tempClass) {
                T tempComponent = (T) tempClass.newInstance();
                tempComponent.configure(_obj);
                return tempComponent;
            }
        } catch (NoClassDefFoundError | ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName, ex);
        }
        return null;
    }

    @Override
    public <T extends IConfigurableComponent> T loadPrototypeComponent(String _parentPath, JSONObject _obj) {
        String tempMapKey = this.getComponentName(_parentPath, _obj);
        AtomicInteger tempInteger = this.protoTypeIndexMap.computeIfAbsent(tempMapKey, k -> new AtomicInteger(0));
        return this.loadPrototypeComponent(_parentPath, _obj, tempInteger.incrementAndGet());
    }

    /**
     * Get Component Name.
     *
     * @param _parentPath
     * @param _obj
     * @return
     */
    protected String getComponentName(String _parentPath, JSONObject _obj) {
        return this.getComponentName(_parentPath, _obj, -1);
    }

    /**
     * Get component name for the new bean instance.
     *
     * @param _parentPath
     * @param _obj
     * @param _index
     * @return
     */
    protected String getComponentName(String _parentPath, JSONObject _obj, int _index) {
        String tempClassName = _obj.getString(KEY_CLASS_NAME);
        if (StringUtils.isBlank(tempClassName)) {
            throw new ConfigurationException(ExceptionCode.COMPONENT_CLASS_MISSED, "The [" + _obj + "]component class is empty");
        }

        StringBuilder tempBuilder = new StringBuilder(_parentPath == null ? "" : _parentPath);
        tempBuilder.append((char) 92);
        String tempComponentName = _obj.getString(KEY_COMPONENT_NAME);
        if (StringUtils.isBlank(tempComponentName)) {
            int tempIndex = tempClassName.lastIndexOf(".");
            if (tempIndex > 0) {
                tempComponentName = tempClassName.substring(tempIndex + 1);
            } else {
                tempComponentName = tempClassName;
            }
        }
        tempBuilder.append(tempComponentName);
        if(_index >= 0){
            tempBuilder.append("[").append(_index).append("]");
        }
        return tempBuilder.toString();
    }
}
