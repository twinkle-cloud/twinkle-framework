package com.twinkle.framework.struct.context;

import com.twinkle.framework.core.context.model.NormalizedAttributeType;
import com.twinkle.framework.core.context.model.NormalizedContext;
import com.twinkle.framework.struct.error.StructAttributeCopyException;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.type.StructAttribute;
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
public class StructAttributeContextAdapter extends NormalizedContext {
    private static final long serialVersionUID = -2185322722215639575L;
    private transient StructAttribute structAttribute = null;
    private transient boolean structAttributeIsSet = false;
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

    public StructAttribute getNME() {
        return this.structAttribute;
    }

    public void setNME(StructAttribute _attr) {
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
            StructAttribute tempAttr = ((StructAttributeContextAdapter)_ne).getNME();

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
            StructAttribute tempAttr = ((StructAttributeContextAdapter)_context).getNME();

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
    public String toStringWithAttrNames(boolean _withAttrNameFlag, boolean var2, boolean var3, boolean var4, String var5) {
        StringBuilder tempBuilder = new StringBuilder();
        if (var2) {
            tempBuilder.append(super.toStringWithAttrNames(_withAttrNameFlag, var2, var3, var4, var5));
        }

        if (var3 && this.structAttribute != null) {
            String tempLineSeparator = System.getProperty("line.separator", "\n");
            if (var4) {
                if (var2) {
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
}
