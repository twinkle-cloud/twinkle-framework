package com.twinkle.framework.core.lang.struct;

import com.twinkle.framework.core.error.TypeAlreadyExistsException;
import com.twinkle.framework.core.error.TypeNotFoundException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 3:04 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DefaultMutableStructTypeManager extends DefaultStructTypeManager {

    @Override
    public void removeType(String _typeName) throws TypeNotFoundException {
        super.removeType(_typeName);
    }

    /**
     * Rename the struct type.
     *
     * @param _oldName
     * @param _newName
     * @throws TypeNotFoundException
     * @throws TypeAlreadyExistsException
     */
    public void renameType(String _oldName, String _newName) throws TypeNotFoundException, TypeAlreadyExistsException {
        StructType var3 = this.getType(_oldName);
        this.addType(_newName, var3);
        this.removeType(_oldName);
    }
}
