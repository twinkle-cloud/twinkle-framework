package com.twinkle.framework.api.component;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/23/19 11:24 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public abstract class AbstractConfigurableComponent implements IConfigurableComponent {
    /**
     * The name of this rule.
     */
    private String name;
    /**
     * The parent path.
     */
    private String parentPath;
    /**
     * The full path name of this rule.
     */
    private String fullPathName;

    public AbstractConfigurableComponent() {
        this.name = "";
        this.fullPathName = "";
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.name = _conf.getString(KEY_NAME);
        if (StringUtils.isBlank(this.name)) {
            String tempClassName = _conf.getString(KEY_CLASS_NAME);
            if (StringUtils.isNotBlank(tempClassName)) {
                this.name = tempClassName.substring(tempClassName.lastIndexOf(".") + 1);
            }
        }
    }

    @Override
    public void setAliasName(String _aliasName) {
        this.name = _aliasName;
    }
    @Override
    public String getFullPathName() {
        if(StringUtils.isBlank(this.name)) {
            return StringUtils.isBlank(this.parentPath)? "" : this.parentPath;
        }

        this.fullPathName = (StringUtils.isBlank(this.parentPath)? "" : this.parentPath) + "\\" + this.name;
        return this.fullPathName;
    }

    @Override
    public void setParentPath(String _parentPath) {
        if (StringUtils.isNotBlank(_parentPath)) {
            this.parentPath = _parentPath;
            this.fullPathName = this.parentPath + "\\" + this.name;
        }
    }

    /**
     * Get component name.
     * FullPathName + _name
     *
     * @param _name
     * @return
     */
    protected String getComponentName(String _name) {
        StringBuilder tempBuilder = new StringBuilder(this.getFullPathName());
        tempBuilder.append((char) 92);
        tempBuilder.append(_name);
        return tempBuilder.toString();
    }
}
