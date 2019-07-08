package com.twinkle.framework.core.asm.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-26 15:33<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ClassDefine implements Define {
    /**
     * Class Access Type: public / protected / private, static, final
     */
    private int access = Opcodes.ACC_PUBLIC;
    /**
     * Class Name.
     */
    @NonNull
    private String name;
    /**
     * Full class name.
     * demo:
     * com/twinkle/cloud/core/usermgmt/controller/Hello2Controller
     */
    @NonNull
    private String internalName;
    /**
     * No need set value.
     * will generate the signature based on the generic type.
     */
    private String signature;
    /**
     * The annotation define list for this class.
     */
    private List<AnnotationDefine> annotationDefineList;
    /**
     * The field list for this class.
     */
    private List<FieldDefine> fieldDefineList;
    /**
     * The method list for this class.
     */
    private List<MethodDefine> methodDefineList;

    /**
     * The super class's type define.
     */
    private TypeDefine superTypeDefine;

    /**
     * The interfaces type define for this class.
     */
    private List<TypeDefine> interfaceTypeDefineList;

    /**
     * Get the SuperName.
     *
     * @return
     */
    public String getSuperInternalName(){
        if(this.superTypeDefine == null) {
            return Type.getInternalName(Object.class);
        }
        return Type.getInternalName(this.superTypeDefine.getTypeClass());
    }

    /**
     * Get the interfaces' name list.
     *
     * @return
     */
    public List<String> getInterfacesInternalName() {
        if(CollectionUtils.isEmpty(this.interfaceTypeDefineList)) {
            return new ArrayList<>(0);
        }
        return this.interfaceTypeDefineList.stream().map(item -> Type.getInternalName(item.getTypeClass())).collect(Collectors.toList());
    }

    /**
     * Add some annotation define into the annotation list.
     *
     * @param _annotationDefine
     */
    public void addAnnotationDefine(AnnotationDefine... _annotationDefine) {
        if(_annotationDefine == null || _annotationDefine.length == 0) {
            return;
        }
        if(this.annotationDefineList == null) {
            this.annotationDefineList = new ArrayList<>();
        }
        this.annotationDefineList.addAll(Arrays.asList(_annotationDefine));
    }

    /**
     * Add some field define into the field list.
     *
     * @param _fieldDefine
     */
    public void addFieldDefine(FieldDefine... _fieldDefine) {
        if(_fieldDefine == null || _fieldDefine.length == 0) {
            return;
        }
        if(this.fieldDefineList == null) {
            this.fieldDefineList = new ArrayList<>();
        }
        this.fieldDefineList.addAll(Arrays.asList(_fieldDefine));
    }

    /**
     * Add some method define into the method list.
     *
     * @param _methodDefine
     */
    public void addMethodDefine(MethodDefine... _methodDefine) {
        if(_methodDefine == null || _methodDefine.length == 0) {
            return;
        }
        if(this.methodDefineList == null) {
            this.methodDefineList = new ArrayList<>();
        }
        this.methodDefineList.addAll(Arrays.asList(_methodDefine));
    }

    /**
     * Add interface type.
     *
     * @param _typeDefine
     */
    public void addInterfaceTypeDefine(TypeDefine... _typeDefine) {
        if(_typeDefine == null || _typeDefine.length == 0) {
            return;
        }
        if(this.interfaceTypeDefineList == null) {
            this.interfaceTypeDefineList = new ArrayList<>();
        }
        this.interfaceTypeDefineList.addAll(Arrays.asList(_typeDefine));
    }
    @Override
    public String toString(){
        StringBuilder tempBuilder = new StringBuilder("[access:");
        tempBuilder.append(this.access);
        tempBuilder.append(", name:");
        tempBuilder.append(this.name);
        tempBuilder.append(", internalName:");
        tempBuilder.append(this.internalName);
        tempBuilder.append(", signature:");
        tempBuilder.append(this.signature);
        tempBuilder.append(", methods:");
        tempBuilder.append(this.methodDefineList == null ? 0: this.methodDefineList.size());
        tempBuilder.append("]");
        return tempBuilder.toString();
    }
}
