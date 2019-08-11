package com.twinkle.framework.core.datastruct.schema;

import com.twinkle.framework.core.datastruct.builder.AnnotationDefBuilder;
import com.twinkle.framework.core.datastruct.builder.TypeDefBuilder;
import com.twinkle.framework.core.datastruct.descriptor.BeanTypeDescriptor;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
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
@Getter
public class BeanTypeDefImpl extends BeanRefTypeDefImpl implements BeanTypeDef, Cloneable {
    private BeanTypeDescriptor descriptor;
    private List<TypeDef> parents;
    private List<AttributeDef> attributes;
    private List<AnnotationDef> annotations;

    public BeanTypeDefImpl(BeanTypeDescriptor _descriptor, ClassLoader _classLoader) throws ClassNotFoundException {
        this(_descriptor, _classLoader, new HashMap());
    }

    public BeanTypeDefImpl(BeanTypeDescriptor _descriptor, ClassLoader _classLoader, Map<String, TypeDef> _typeDefMap) throws ClassNotFoundException {
        super(_descriptor.getName(), TypeDefBuilder.getObjectType(_descriptor.getClassName()));
        this.descriptor = _descriptor;
        _typeDefMap.put(_descriptor.getClassName(), this);
        this.parents = this.initBeanParents(_descriptor, _classLoader, _typeDefMap);
        this.attributes = this.initBeanAttributes(_descriptor, _classLoader, _typeDefMap);
        this.annotations = this.initAnnotations(_descriptor.getAnnotations(), _classLoader);
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

    /**
     * Initialize the Bean's parents.
     *
     * @param _descriptor
     * @param _classLoader
     * @param _typeDefineMap
     * @return
     * @throws ClassNotFoundException
     */
    protected List<TypeDef> initBeanParents(BeanTypeDescriptor _descriptor, ClassLoader _classLoader, Map<String, TypeDef> _typeDefineMap) throws ClassNotFoundException {
        if(CollectionUtils.isEmpty(_descriptor.getParents())) {
            return Collections.EMPTY_LIST;
        }
        return _descriptor.getParents().stream().map(item -> {
                    try {
                        return TypeDefBuilder.getTypeDef(item, _classLoader, _typeDefineMap);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList());
    }

    protected List<AttributeDef> initBeanAttributes(BeanTypeDescriptor _descriptor, ClassLoader _classLoader, Map<String, TypeDef> _typeDefineMap) throws ClassNotFoundException {
        if(CollectionUtils.isEmpty(_descriptor.getAttributes())) {
            return Collections.EMPTY_LIST;
        }
        return _descriptor.getAttributes().stream().map(item -> {
                    try {
                        List<AnnotationDef> tempAnnotationDefineList = this.initAnnotations(item.getAnnotations(), _classLoader);
                        return new AttributeDefImpl(item, TypeDefBuilder.getTypeDef(item.getType(), _classLoader, _typeDefineMap), tempAnnotationDefineList);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList());
    }

    protected List<AnnotationDef> initAnnotations(Set<String> _annotationSet, ClassLoader _classLoader) throws ClassNotFoundException {
        return _annotationSet.stream().filter(item -> item.startsWith("@")).map(item -> {
            try {
                return AnnotationDefBuilder.getAnnotationDef(item, _classLoader);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
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
