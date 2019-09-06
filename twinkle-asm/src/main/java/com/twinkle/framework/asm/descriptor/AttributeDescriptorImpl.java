package com.twinkle.framework.asm.descriptor;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;
import org.objectweb.asm.Opcodes;

import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 14:18<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class AttributeDescriptorImpl implements AttributeDescriptor {
    private int access = Opcodes.ACC_PRIVATE;
    private final String name;
    private final Object defaultValue;
    private final boolean isRequired;
    private final boolean isReadOnly;
    private final Set<String> annotations;
    private TypeDescriptor type;
    /**
     * ExtraInfo for this attribute.
     */
    private JSONObject extraInfo;

    private AttributeDescriptorImpl(String _name, Object _defaultValue, boolean _isRequired, boolean _isReadOnly, Set<String> _annotations, TypeDescriptor _type, JSONObject _extInfo) {
        this.name = _name;
        this.defaultValue = _defaultValue;
        this.isRequired = _isRequired;
        this.isReadOnly = _isReadOnly;
        this.annotations = _annotations;
        this.type = _type;
        this.extraInfo = _extInfo;
    }

    private AttributeDescriptorImpl(int _access, String _name, Object _defaultValue, boolean _isRequired, boolean _isReadOnly, Set<String> _annotations, TypeDescriptor _type, JSONObject _extInfo) {
        this.access = _access;
        this.name = _name;
        this.defaultValue = _defaultValue;
        this.isRequired = _isRequired;
        this.isReadOnly = _isReadOnly;
        this.annotations = _annotations;
        this.type = _type;
        this.extraInfo = _extInfo;
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("AttributeDescriptorImpl [\n_name=").append(this.name).append(", \n_annotations=").append(this.annotations).append(", \n_defaultValue=").append(this.defaultValue).append(", \n_isReadOnly=").append(this.isReadOnly).append(", \n_isRequired=").append(this.isRequired).append("\n]");
        return tempBuilder.toString();
    }

    @Override
    public Object getExtraInfoByKey(String _key) {
        if (this.extraInfo == null)
            return null;
        return this.extraInfo.get(_key);
    }
}
