package com.twinkle.framework.core.datastruct.define;

import com.twinkle.framework.core.datastruct.builder.TypeDefBuilder;
import com.twinkle.framework.core.datastruct.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.core.utils.TypeDefUtil;
import lombok.Data;
import org.objectweb.asm.Type;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 22:04<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public class BeanTypeDefImpl extends BeanRefTypeDefImpl implements BeanTypeDef, Cloneable {
    private BeanTypeDescriptor descriptor;
    private List<TypeDef> interfaceTypeDefs;
    private List<AttributeDef> attributes;
    private TypeDef superTypeDef;
    private List<AnnotationDef> annotations;
    private Map<String, TypeDef> typeDefMap;

    public BeanTypeDefImpl(BeanTypeDescriptor _descriptor, ClassLoader _classLoader) throws ClassNotFoundException {
        this(_descriptor, _classLoader, new HashMap());
    }

    public BeanTypeDefImpl(BeanTypeDescriptor _descriptor, ClassLoader _classLoader, Map<String, TypeDef> _typeDefMap) throws ClassNotFoundException {
        super(_descriptor.getName(), TypeDefBuilder.getObjectType(_descriptor.getClassName()));
        this.descriptor = _descriptor;
        this.typeDefMap = _typeDefMap;
        this.typeDefMap.put(_descriptor.getClassName(), this);
        this.superTypeDef = TypeDefBuilder.getTypeDef(_descriptor.getSuperDescriptor(), _classLoader, _typeDefMap);
        this.interfaceTypeDefs = TypeDefUtil.getParents(_descriptor.getInterfaceDescriptors(), _classLoader, _typeDefMap);
        this.attributes = TypeDefUtil.getAttributes(_descriptor.getAttributes(), _classLoader, _typeDefMap);
        this.annotations = TypeDefUtil.getAnnotations(_descriptor.getAnnotations(), _classLoader);
    }

    public BeanTypeDefImpl(BeanTypeDefImpl _beanTypeDefine) {
        super(_beanTypeDefine.getName(), _beanTypeDefine.getType());
        this.descriptor = _beanTypeDefine.getDescriptor();
        this.superTypeDef = _beanTypeDefine.getSuperTypeDef();
        this.interfaceTypeDefs = new ArrayList(_beanTypeDefine.getInterfaceTypeDefs());
        this.attributes = new ArrayList(_beanTypeDefine.getAttributes());
        this.annotations = new ArrayList(_beanTypeDefine.getAnnotations());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BeanTypeDefImpl tempDest = (BeanTypeDefImpl) super.clone();
        tempDest.superTypeDef = this.superTypeDef;
        tempDest.interfaceTypeDefs = new ArrayList(this.interfaceTypeDefs);
        tempDest.attributes = new ArrayList(this.attributes);
        tempDest.annotations = new ArrayList(this.annotations);
        return tempDest;
    }

    @Override
    public TypeDef addInterfaceTypeDef(Type _interfaceType) {
        Optional<TypeDef> tempResult = this.interfaceTypeDefs.stream().parallel()
                .filter(item -> _interfaceType.equals(item.getType())).findAny();
        if(tempResult.isPresent()) {
            return tempResult.get();
        }
        return new BeanRefTypeDefImpl(_interfaceType.getClassName(), _interfaceType);
    }

    @Override
    public List<String> getInterfaces() {
        return this.interfaceTypeDefs.stream().map(item -> item.getType().getClassName()).collect(Collectors.toList());
    }
}
