package com.twinkle.framework.struct.type;

import com.twinkle.framework.struct.error.TypeAlreadyExistsException;
import com.twinkle.framework.struct.error.TypeNotFoundException;

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
        StructType tempType = this.getType(_oldName);
        this.addType(_newName, tempType);
        this.removeType(_oldName);
    }
}
