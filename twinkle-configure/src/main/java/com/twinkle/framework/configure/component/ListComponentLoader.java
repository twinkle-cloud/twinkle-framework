package com.twinkle.framework.configure.component;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.IConfigurableComponent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListComponentLoader<T extends IConfigurableComponent> {
    @Getter
    private List<T> componentList = new ArrayList<>(8);
    @Getter
    private Map<String, T> componentMap = new HashMap<>(8);

    /**
     * Load the components.
     *
     * @param _listNamesKey
     * @param _listComponentKey
     * @param _parentPath
     * @param _conf
     * @return
     */
    public List<T> loadListComponent(String _listNamesKey, String _listComponentKey, String _parentPath, JSONObject _conf){
        List<T> _components = new ArrayList<>(8);
        JSONArray tempArray = _conf.getJSONArray(_listNamesKey);
        JSONArray tempBeanArray = _conf.getJSONArray(_listComponentKey);
        if (tempArray.isEmpty() || tempBeanArray.isEmpty()) {
            return _components;
        }

        for (int i = 0; i < tempArray.size(); i++) {
            String tempItem = tempArray.getString(i);
            for (int j = 0; j < tempBeanArray.size(); j++) {
                JSONObject tempObj = tempBeanArray.getJSONObject(j);
                if (tempObj.getString("Name").equals(tempItem)) {
                    T tempComponent = ComponentFactory.getInstance().loadComponent(_parentPath, tempObj);

                    _components.add(tempComponent);
                    componentMap.put(tempItem, tempComponent);
                    break;
                }
            }
        }
        this.componentList = _components;
        return this.componentList;
    }

    /**
     * Get componet list by keys
     * Must after loadListComponent().
     *
     * @param _keys
     * @return
     */
    public List<T> getComponentListByKeys(List<String> _keys) {
        List<T> _components = new ArrayList<>(8);
        for (String tempKey : _keys) {
            T tempComponent = componentMap.get(tempKey);
            if (tempComponent != null) {
                _components.add(tempComponent);
            }
        }
        return _components;
    }
}
