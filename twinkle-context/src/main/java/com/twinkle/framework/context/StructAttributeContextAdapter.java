package com.twinkle.framework.context;

import com.twinkle.framework.api.context.NormalizedAttributeType;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.context.model.DefaultNormalizedContext;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.error.StructAttributeCopyException;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.utils.StructAttributePrintFormatter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/10/19 9:34 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeContextAdapter extends DefaultNormalizedContext {
    private static final long serialVersionUID = -2185322722215639575L;
    private transient StructAttribute structAttribute = null;
    private transient boolean structAttributeIsSet = false;
    private static final StructType ROOT_STRUCT_TYPE = StructAttributeSchemaManager.getStructAttributeSchema().getRootNMEType();
    private static final StructAttributeFactory STRUCT_ATTRIBUTE_SCHEMA_FACTORY = StructAttributeSchemaManager.getStructAttributeFactory();
    public StructAttributeContextAdapter() {
    }

    public StructAttributeContextAdapter(String _neTypeName) {
        super(_neTypeName);
    }

    public StructAttributeContextAdapter(String _neTypeName, boolean _createFlag) {
        super(_neTypeName, _createFlag);
    }

    public StructAttributeContextAdapter(NormalizedAttributeType _neType) {
        super(_neType);
    }

    public StructAttributeContextAdapter(NormalizedAttributeType _neType, boolean _createFlag) {
        super(_neType, _createFlag);
    }

    public StructAttribute getStructAttribute() {
        return this.structAttribute;
    }

    public void setStructAttribute(StructAttribute _attr) {
        this.structAttribute = _attr;
        this.structAttributeIsSet = _attr != null;
    }

    @Override
    public void clear() {
        super.clear();
        if (this.structAttribute != null) {
            this.structAttribute.clear();
            this.structAttribute = null;
            this.structAttributeIsSet = false;
        }

    }

    @Override
    public void clearValues() {
        super.clearValues();
        if (this.structAttribute != null) {
            this.structAttribute.clear();
        }

    }
    @Override
    public Object clone() {
        StructAttributeContextAdapter tempAdapter = (StructAttributeContextAdapter)super.clone();
        if (this.structAttribute != null) {
            try {
                tempAdapter.structAttribute = this.structAttribute.duplicate();
                tempAdapter.structAttributeIsSet = this.structAttributeIsSet;
            } catch (StructAttributeCopyException e) {
                throw new UnsupportedOperationException("Error while cloning the NME");
            }
        }

        return tempAdapter;
    }
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && !this.structAttributeIsSet;
    }
    @Override
    public void set(NormalizedContext _ne) {
        super.set(_ne);
        if (_ne instanceof StructAttributeContextAdapter) {
            StructAttribute tempAttr = ((StructAttributeContextAdapter)_ne).getStructAttribute();

            try {
                if (tempAttr == null) {
                    this.structAttribute = null;
                    this.structAttributeIsSet = false;
                } else if (this.structAttribute != null && this.structAttribute.getType().equals(tempAttr.getType())) {
                    tempAttr.copy(this.structAttribute);
                    this.structAttributeIsSet = ((StructAttributeContextAdapter)_ne).structAttributeIsSet;
                } else {
                    this.structAttribute = tempAttr.duplicate();
                    this.structAttributeIsSet = ((StructAttributeContextAdapter)_ne).structAttributeIsSet;
                }
            } catch (StructAttributeException e) {
                throw new UnsupportedOperationException("Cannot set to NME.");
            }
        }

    }
    @Override
    public void update(NormalizedContext _context) {
        super.update(_context);
        if (_context instanceof StructAttributeContextAdapter) {
            StructAttribute tempAttr = ((StructAttributeContextAdapter)_context).getStructAttribute();

            try {
                if (tempAttr  == null) {
                    this.structAttribute = null;
                    this.structAttributeIsSet = false;
                } else if (this.structAttribute != null) {
                    if (!this.structAttribute.getType().equals(tempAttr .getType())) {
                        throw new IllegalArgumentException("Can't update an NME with type " + this.structAttribute.getType().getName() + " with an NME of type " + tempAttr .getType().getName());
                    }

                    tempAttr .copy(this.structAttribute);
                    this.structAttributeIsSet = ((StructAttributeContextAdapter)_context).structAttributeIsSet;
                } else {
                    this.structAttribute = tempAttr .duplicate();
                    this.structAttributeIsSet = ((StructAttributeContextAdapter)_context).structAttributeIsSet;
                }
            } catch (StructAttributeException e) {
                throw new UnsupportedOperationException("Cannot set to NME.");
            }
        }

    }

    @Override
    public String toStringValuesOnly() {
        StringBuilder tempBuilder = (new StringBuilder()).append(super.toStringValuesOnly());
        if (this.structAttribute != null) {
            String tempSeparator = System.getProperty("line.separator", "\n");
            tempBuilder.append(tempSeparator);
            StructAttributePrintFormatter.append(this.structAttribute, tempBuilder, "", tempSeparator);
        }

        return tempBuilder.toString();
    }
    @Override
    public String toStringWithAttrNames(boolean _printNullFlag, boolean _logAttrFlag, boolean _logStructAttributeFlag, boolean _inSingleLineFlag, String var5) {
        StringBuilder tempBuilder = new StringBuilder();
        if (_logAttrFlag) {
            tempBuilder.append(super.toStringWithAttrNames(_printNullFlag, _logAttrFlag, _logStructAttributeFlag, _inSingleLineFlag, var5));
        }

        if (_logStructAttributeFlag && this.structAttribute != null) {
            String tempLineSeparator = System.getProperty("line.separator", "\n");
            if (_inSingleLineFlag) {
                if (_logAttrFlag) {
                    tempBuilder.append(var5);
                }

                tempBuilder.append(this.structAttribute.toString());
            } else {
                tempBuilder.append(var5);
                StructAttributePrintFormatter.append(this.structAttribute, tempBuilder, "", tempLineSeparator);
            }
        }

        return tempBuilder.toString();
    }

    private void initRootStructAttribute() {
        if (ROOT_STRUCT_TYPE != null) {
            StructAttribute tempAttr = STRUCT_ATTRIBUTE_SCHEMA_FACTORY.newStructAttribute(ROOT_STRUCT_TYPE);
            this.setStructAttribute(tempAttr);
        }

    }
}
