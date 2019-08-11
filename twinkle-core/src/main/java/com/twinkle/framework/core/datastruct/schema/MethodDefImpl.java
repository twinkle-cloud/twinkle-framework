package com.twinkle.framework.core.datastruct.schema;

import com.twinkle.framework.core.utils.TypeUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-10 22:14<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Slf4j
public class MethodDefImpl implements MethodDef, Cloneable {
    /**
     * Method's access.
     */
    @NonNull
    private int access;
    /**
     * Method's name.
     */
    @NonNull
    private String name;
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
    private TypeDef returnType;
    /**
     * Method's parameters list.
     */
    private List<AttributeDef> parameterAttrs;
    /**
     * Method's local parameters list.
     *
     */
    private List<AttributeDef> localParameterAttrs;
    /**
     * Method's AnnotationList.
     */
    private List<AnnotationDef> annotations;
    /**
     * The exceptions of this method.
     */
    private List<TypeDef> exceptions;

    public String getDescriptor(){
        return this.packDescriptor();
    }
    /**
     * Get Method Descriptor.
     *
     * @return
     */
    private String packDescriptor() {
        Type returnType = this.getReturnType().getType();
        if(CollectionUtils.isEmpty(this.parameterAttrs)) {
            return Type.getMethodDescriptor(returnType);
        }
        Type[] parameterTypeList = new Type[this.parameterAttrs.size()];
        int tempIndex = 0;
        for(AttributeDef tempDefine : this.parameterAttrs) {
            parameterTypeList[tempIndex] = tempDefine.getType().getType();
            tempIndex ++;
        }
        return Type.getMethodDescriptor(returnType, parameterTypeList);
    }
    /**
     * Pack the detail signature.
     *
     * @return
     */
//    private String packDetailSignature() {
//        StringBuilder tempBuilder = new StringBuilder();
//        if(CollectionUtils.isEmpty(this.parameterAttrs)) {
//            return tempBuilder.toString();
//        }
//        tempBuilder.append("(");
//        for(AttributeDef tempDefine : this.parameterAttrs) {
//            tempBuilder.append(TypeUtil.getTypeSignature(tempDefine.getTypeDefine()));
//        }
//        tempBuilder.append(")");
//        tempBuilder.append(TypeUtil.getTypeSignature(this.returnTypeDefine));
//        if(tempBuilder.indexOf("<") < 0) {
//            log.info("There is no generic type found, so return the empty signature.");
//            return "";
//        }
//        return tempBuilder.toString();
//    }
}
