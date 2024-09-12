package com.twinkle.framework.api.component;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/23/19 11:12 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IComponent {
    String KEY_NAME = "Name";
    String KEY_CLASS_NAME = "ClassName";
    /**
     * Update the parent path of the component.
     *
     * @param _parentPath
     */
    void setParentPath(String _parentPath);

    /**
     * Update the name with aliasName.
     *
     * @param _aliasName
     */
    void setAliasName(String _aliasName);
    /**
     * Get the short name of this component.
     */
    String getName();

    /**
     * Get the full path name of this component.
     */
    String getFullPathName();
}
