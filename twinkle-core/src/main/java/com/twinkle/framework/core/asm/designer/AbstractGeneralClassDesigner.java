package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.datastruct.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-13 10:33<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractGeneralClassDesigner extends AbstractGeneralBeanClassDesigner {
    private Map<String, Map<String, LocalAttributeIndexInfo>> methodAttributeIndexMap;

    public AbstractGeneralClassDesigner(String _className, GeneralClassTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
        this.methodAttributeIndexMap = new HashMap<>(_beanTypeDef.getMethods().size());
    }

    /**
     * Add the class declaration and add the annotations.
     *
     * @param _visitor
     * @param _className
     * @param _superName
     * @param _interfaceList
     * @param _beanTypeDef
     * @return
     */
    @Override
    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor, String _className, String _superName, List<String> _interfaceList, BeanTypeDef _beanTypeDef) {
        super.addClassDeclaration(_visitor, _className, _superName, _interfaceList, _beanTypeDef);
        _beanTypeDef.getAnnotations().stream().forEach(item -> this.addClassAnnotation(_visitor, item));
        return _visitor;
    }

    @Override
    protected void addClassDefinition(ClassVisitor _visitor, String _className, String _superName, List<AttributeDef> _attrDefList, BeanTypeDef _beanTypeDef) {
        this.addDefaultConstants(_visitor, _className, _attrDefList);
        this.addFields(_visitor, _attrDefList);
        this.addDefaultConstructorDefinition(_visitor, _className, _superName, _attrDefList);
    }

    /**
     * Add methods to this class.
     *
     * @param _visitor
     * @param _className
     * @param _methodDefList
     */
    protected void addMethodsDefinition(ClassVisitor _visitor, String _className, List<MethodDef> _methodDefList) {
        _methodDefList.parallelStream().forEach(item -> {
            try {
                Map<String, LocalAttributeIndexInfo> tempIndexMap = new HashMap<>(1 + item.getLocalParameterAttrs().size() + item.getParameterAttrs().size());
                this.methodAttributeIndexMap.put(item.getName(), tempIndexMap);
                this.addMethodDefinition(_visitor, _className, item);
            } catch (IllegalAccessException e) {
                log.error("Encountered Error while add method: [{}], exception: [{}]", item.getName(), e);
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                log.error("Encountered Error while add method: [{}], exception: [{}]", item.getName(), e);
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                log.error("The instruction packing method missing for this method: [{}], exception: [{}]", item.getName(), e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Add method to this class.
     *
     * @param _visitor
     * @param _className
     * @param _methodDef
     * @return
     */
    protected MethodVisitor addMethodDefinition(ClassVisitor _visitor, String _className, MethodDef _methodDef) throws NoSuchMethodException, SecurityException, InvocationTargetException, IllegalAccessException {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, _methodDef.getName(), _methodDef.getDescriptor(), _methodDef.getSignature(), this.getExceptions(_methodDef.getExceptions()));
        _methodDef.getAnnotations().stream().forEach(item -> this.addMethodAnnotation(tempVisitor, item, AnnotationDef.Kind.METHOD));
        this.addMethodParameter(tempVisitor, _methodDef.getParameterAttrs());
        tempVisitor.visitCode();
        // Pack the instruction body of this method.
        Method tempInstructionMethod = this.getClass().getMethod(_methodDef.getInstructionMethodName(), MethodVisitor.class, String.class, MethodDef.class);
        tempInstructionMethod.invoke(this, tempVisitor, _className, _methodDef);

        for(AttributeDef tempAttrDef : _methodDef.getLocalParameterAttrs()) {
            String tempSignature = "";
            if(tempAttrDef.getType().isGeneric()) {
                tempSignature = ((GenericTypeDef) tempAttrDef.getType()).getFieldSignature();
            }
            LocalAttributeIndexInfo tempIndexInfo = this.methodAttributeIndexMap.get(_methodDef.getName()).get(tempAttrDef.getName());
            if(tempIndexInfo!= null) {
                tempVisitor.visitLocalVariable(tempAttrDef.getFieldName(),
                        tempAttrDef.getType().getType().getDescriptor(),
                        tempSignature,
                        tempIndexInfo.getStartLabel(),
                        tempIndexInfo.getEndLabel(),
                        tempIndexInfo.getIndex());
            }
        }
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add the local attribute's index into the map.
     *
     * @param _methodName
     * @param _attrName
     * @param _indexInfo
     */
    protected void addMethodAttributeIndex(String _methodName, String _attrName, LocalAttributeIndexInfo _indexInfo) {
        this.methodAttributeIndexMap.get(_methodName).put(_attrName, _indexInfo);
    }

    /**
     * Add method's parameters.
     *
     * @param _visitor
     * @param _paramList
     * @return
     */
    protected MethodVisitor addMethodParameter(MethodVisitor _visitor, List<AttributeDef> _paramList) {
        if(CollectionUtils.isEmpty(_paramList)) {
            return _visitor;
        }
        for(int i = 0; i<_paramList.size(); i++) {
            AttributeDef tempDef = _paramList.get(i);
            _visitor.visitParameter(tempDef.getFieldName(), tempDef.getAccess());
            List<AnnotationDef> tempParamAnnotations = tempDef.getAnnotations();
            for(AnnotationDef tempAnnotation : tempParamAnnotations) {
                AnnotationVisitor tempVisitor = _visitor.visitParameterAnnotation(i, tempAnnotation.getType().getDescriptor(), true);
                this.addAnnotationElements(tempVisitor, tempAnnotation);
                tempVisitor.visitEnd();
            }
        }

        return _visitor;
    }

    /**
     * Get Exception internal class name Array.
     *
     * @param _typeList
     * @return
     */
    private String[] getExceptions(List<TypeDef> _typeList) {
        if (CollectionUtils.isEmpty(_typeList)) {
            return null;
        }
        List<String> tempResult = _typeList.stream().map(item -> item.getType().getInternalName()).collect(Collectors.toList());
        return tempResult.toArray(new String[]{});
    }

    @Override
    protected int initAccessFlags() {
        return Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER;
    }
}
