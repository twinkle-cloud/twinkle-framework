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
    /**
     * Update the name of the component.
     *
     * @param _name
     */
    void setFullPathName(String _name);

    /**
     * Get the short name of this component.
     */
    String getName();

    /**
     * Get the full path name of this component.
     */
    String getFullPathName();
}
