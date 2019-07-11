package com.twinkle.framework.core.asm.data;

import com.twinkle.framework.core.utils.TypeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class MethodDefine implements Define {
    /**
     * Method's access.
     */
    @NonNull
    private int access;
    /**
     * Class Name.
     */
    @NonNull
    private String name;
    /**
     * Parameters' list.
     */
    private List<ParameterDefine> parameterDefineList;
    /**
     * No need to set the value.
     * Will generate the value with parameter list and return class.
     */
    private String descriptor;
    /**
     * No need to set the value.
     * Will generate the value with parameter list and return class.
     */
    private String signature;
    /**
     * Return Type.
     */
    @NonNull
    private TypeDefine returnTypeDefine;
    /**
     * The Exception Type define list.
     */
    private List<TypeDefine> exceptionDefineList;
    /**
     * Method's AnnotationList.
     */
    private List<AnnotationDefine> annotationDefineList;
    /**
     * Local Parameters.
     */
    private List<ParameterDefine> localParameterDefineList;

    /**
     * Get the class define.
     */
    private ClassDefine classDefine;

    /**
     * Max Stacks.
     */
    private int maxStacks;
    /**
     * Max Local attributes.
     */
    private int maxLocals;

    /**
     * Add Parameter define into the parameter list.
     * @param _parameterDefine
     */
    public void addParameterDefine(ParameterDefine... _parameterDefine) {
        if(CollectionUtils.isEmpty(this.parameterDefineList)) {
            this.parameterDefineList = new ArrayList<>();
        }
        if(_parameterDefine == null || _parameterDefine.length == 0){
            return;
        }
        this.parameterDefineList.addAll(Arrays.asList(_parameterDefine));
    }
    /**
     * Add Local Parameters.
     *
     * @param _parameterDefine
     */
    public void addLocalParameterDefine(ParameterDefine... _parameterDefine) {
        if(CollectionUtils.isEmpty(this.localParameterDefineList)) {
            this.localParameterDefineList = new ArrayList<>();
        }
        if(_parameterDefine == null || _parameterDefine.length == 0){
            return;
        }
        this.localParameterDefineList.addAll(Arrays.asList(_parameterDefine));
    }
    /**
     * Add AnnotationDefine for method annoatations.
     *
     * @param _annotationDefine
     */
    public void addAnnotationDefine(AnnotationDefine... _annotationDefine){
        if(CollectionUtils.isEmpty(this.annotationDefineList)) {
            this.annotationDefineList = new ArrayList<>();
        }
        if(_annotationDefine == null || _annotationDefine.length == 0) {
            return;
        }
        this.annotationDefineList.addAll(Arrays.asList(_annotationDefine));
    }

    /**
     * Get the descriptor of this method.
     *
     * @return
     */
    public String getDescriptor() {
        if(this.descriptor == null) {
            this.setDescriptor(this.packDescriptor());
        }
        return this.descriptor;
    }

    /**
     * Get the signature.
     *
     * @return
     */
    public String getSignature() {
        if(this.signature == null) {
            this.setSignature(this.packDetailSignature());
        }
        return this.signature;
    }

    /**
     * Get Exceptions' internalNames List.
     *
     * @return
     */
    public String[] getExceptions() {
        if(CollectionUtils.isEmpty(this.exceptionDefineList)) {
            return new String[]{};
        }
        String[] tempExceptionArray = new String[this.exceptionDefineList.size()];
        int tempIndex = 0;
        for(TypeDefine tempDefine : this.exceptionDefineList) {
            tempExceptionArray[tempIndex] = Type.getInternalName(tempDefine.getTypeClass());
            tempIndex ++;
        }
        return tempExceptionArray;
    }

    /**
     * Get Method Descriptor.
     *
     * @return
     */
    private String packDescriptor() {
        Type returnType = this.getReturnType();
        if(CollectionUtils.isEmpty(this.parameterDefineList)) {
           return Type.getMethodDescriptor(returnType);
        }
        Type[] parameterTypeList = new Type[this.parameterDefineList.size()];
        int tempIndex = 0;
        for(ParameterDefine tempDefine : this.parameterDefineList) {
            parameterTypeList[tempIndex] = Type.getType(tempDefine.getTypeDefine().getTypeClass());
            tempIndex ++;
        }
        return Type.getMethodDescriptor(returnType, parameterTypeList);
    }

    /**
     * Get Return Type.
     * @return
     */
    private Type getReturnType() {
        if(this.returnTypeDefine == null) {
            return Type.getType(Void.TYPE);
        }
        Class<?> tempClass = this.returnTypeDefine.getTypeClass();
        if(tempClass == null) {
            return Type.getType(Void.TYPE);
        }
        return Type.getType(tempClass);
    }

    /**
     * Pack the detail signature.
     *
     * @return
     */
    private String packDetailSignature() {
        StringBuilder tempBuilder = new StringBuilder();
        if(CollectionUtils.isEmpty(this.parameterDefineList)) {
            return tempBuilder.toString();
        }
        tempBuilder.append("(");
        for(ParameterDefine tempDefine : this.parameterDefineList) {
            tempBuilder.append(TypeUtil.getTypeSignature(tempDefine.getTypeDefine()));
        }
        tempBuilder.append(")");
        tempBuilder.append(TypeUtil.getTypeSignature(this.returnTypeDefine));
        if(tempBuilder.indexOf("<") < 0) {
            log.info("There is no generic type found, so return the empty signature.");
            return "";
        }
        return tempBuilder.toString();
    }

    /**
     * Get invisible init method define for some class.
     *
     * @param _classDefine
     * @return
     */
    public static MethodDefine getInitMethodDefine(){
        MethodDefine tempDefine = new MethodDefine(
                Opcodes.ACC_PUBLIC,
                "<init>",
                new TypeDefine(Void.TYPE)
        );

        return tempDefine;
    }

    /**
     * Get log's initialize static method.
     *
     * @return
     */
    public static MethodDefine getLogInitMethodDefine(){
        MethodDefine tempDefine = new MethodDefine(
                Opcodes.ACC_STATIC,
                "<clinit>",
                new TypeDefine(Void.TYPE)
        );
        return tempDefine;
    }

    public static void main(String[] _args) {
        MethodDefine tempMethodDefine = new MethodDefine();

        ParameterDefine tempP1 = new ParameterDefine();
        TypeDefine tempT1 = new TypeDefine(String.class);
        TypeDefine tempT2 = new TypeDefine(List.class);
        tempT2.addGenericType(tempT1);
        TypeDefine tempT3 = new TypeDefine(Map.class);
        tempT3.addGenericType(tempT1, tempT2);
        tempP1.setName("_requestMap");
        tempP1.setTypeDefine(tempT3);

        ParameterDefine tempP2 = new ParameterDefine();
        TypeDefine tempT4 = new TypeDefine(String.class);
        tempP2.setName("_userName");
        tempP2.setTypeDefine(tempT4);

        tempMethodDefine.addParameterDefine(tempP2, tempP1);

//        TypeDefine tempT5 = new TypeDefine(GeneralContentResult.class);
//        tempT5.addGenericType(tempT1);
//        tempMethodDefine.setReturnTypeDefine(tempT5);

        System.out.println("The desc is:" + tempMethodDefine.getDescriptor());
        System.out.println("The signature is:" + tempMethodDefine.getSignature());
    }
}
