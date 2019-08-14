package com.twinkle.framework.core.datastruct.schema;

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
    private List<TypeDef> parents;
    private List<AttributeDef> attributes;
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
        this.parents = TypeDefUtil.getParents(_descriptor.getParents(), _classLoader, _typeDefMap);
        this.attributes = TypeDefUtil.getAttributes(_descriptor.getAttributes(), _classLoader, _typeDefMap);
        this.annotations = TypeDefUtil.getAnnotations(_descriptor.getAnnotations(), _classLoader);
    }

    public BeanTypeDefImpl(BeanTypeDefImpl _beanTypeDefine) {
        super(_beanTypeDefine.getName(), _beanTypeDefine.getType());
        this.descriptor = _beanTypeDefine.getDescriptor();
        this.parents = new ArrayList(_beanTypeDefine.getParents());
        this.attributes = new ArrayList(_beanTypeDefine.getAttributes());
        this.annotations = new ArrayList(_beanTypeDefine.getAnnotations());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BeanTypeDefImpl newObj = (BeanTypeDefImpl) super.clone();
        newObj.parents = new ArrayList(this.parents);
        newObj.attributes = new ArrayList(this.attributes);
        newObj.annotations = new ArrayList(this.annotations);
        return newObj;
    }

    @Override
    public TypeDef addParent(Type _parentType) {
        Optional<TypeDef> tempResult = this.parents.stream().parallel()
                .filter(item -> _parentType.equals(item.getType())).findAny();
        if(tempResult.isPresent()) {
            return tempResult.get();
        }
        return new BeanRefTypeDefImpl(_parentType.getClassName(), _parentType);
    }

    @Override
    public List<String> getInterfaces() {
        return this.parents.stream().map(item -> item.getType().getClassName()).collect(Collectors.toList());
    }
}
