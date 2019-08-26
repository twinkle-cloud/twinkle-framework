package com.twinkle.framework.api.component;

import lombok.Data;

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
public abstract class AbstractComponent implements IComponent {
    /**
     * The name of this rule.
     */
    private String name;
    /**
     * The full path name of this rule.
     */
    private String fullPathName;

    public AbstractComponent(){
        this.name = "";
        this.fullPathName = "";
    }

    @Override
    public void setFullPathName(String _name) {
        this.fullPathName = _name;
        int tempIndex = _name.lastIndexOf(92);
        if (tempIndex < 0) {
            this.name = _name;
        } else if (tempIndex + 1 < _name.length()) {
            this.name = _name.substring(tempIndex + 1);
        } else {
            this.name = "";
        }
    }
}
