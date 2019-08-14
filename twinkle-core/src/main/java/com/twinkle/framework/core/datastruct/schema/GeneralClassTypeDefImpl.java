package com.twinkle.framework.core.datastruct.schema;

import com.sun.tools.doclets.internal.toolkit.builders.MethodBuilder;
import com.twinkle.framework.core.datastruct.builder.MethodDefBuilder;
import com.twinkle.framework.core.datastruct.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.GeneralClassTypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.MethodTypeDescriptor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-13 11:29<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class GeneralClassTypeDefImpl extends BeanTypeDefImpl implements GeneralClassTypeDef {
    /**
     * Class's methods.
     */
    private List<MethodDef> methods;

    public GeneralClassTypeDefImpl(GeneralClassTypeDescriptor _descriptor, ClassLoader _classLoader) throws ClassNotFoundException {
        super(_descriptor, _classLoader);
        this.methods = MethodDefBuilder.getMethodDefs(_descriptor.getMethods(), _classLoader, this.getTypeDefMap());
    }

    public GeneralClassTypeDefImpl(GeneralClassTypeDescriptor _descriptor, ClassLoader _classLoader, Map<String, TypeDef> _typeDefMap) throws ClassNotFoundException {
        super(_descriptor, _classLoader, _typeDefMap);
        this.methods = MethodDefBuilder.getMethodDefs(_descriptor.getMethods(), _classLoader, _typeDefMap);
    }

    public GeneralClassTypeDefImpl(GeneralClassTypeDefImpl _beanTypeDefine) {
        super(_beanTypeDefine);
        this.methods = _beanTypeDefine.getMethods();
    }
}
