package com.twinkle.framework.core.asm.data;

import org.objectweb.asm.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-26 16:01<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AnnotationDefine {
    /**
     * The Annotation class.
     */
    @NonNull
    private Class<?> annotationClass;
    /**
     * The Annotation's parameter-value.
     */
    private Map<String, Object> valuesMap;

    /**
     * Add annotation's parameter and its'value.
     *
     * @param _key
     * @param _value
     */
    public void addValueIntoMap(String _key, Object _value){
        if(this.valuesMap == null) {
            this.valuesMap = new HashMap(8);
        }
        if(_value == null) {
            return;
        }
        if(_value instanceof EnumAnnotationValueDefine) {
            this.valuesMap.put(_key, new String[]{Type.getDescriptor(
                    ((EnumAnnotationValueDefine) _value).getTypeClass()),
                    ((EnumAnnotationValueDefine) _value).getValue()}
            );
            return;
        }
        if(_value instanceof List){
            Object tempObj = ((List) _value).get(0);
            if(tempObj instanceof EnumAnnotationValueDefine) {
                List<String[]> tempList = new ArrayList<>();
                for(EnumAnnotationValueDefine tempDefine : (List<EnumAnnotationValueDefine>) _value) {
                    tempList.add(new String[]{Type.getDescriptor(
                            tempDefine.getTypeClass()),
                            tempDefine.getValue()});
                }
                this.valuesMap.put(_key, tempList);
            } else {
                this.valuesMap.put(_key, _value);
            }
        } else {
            this.valuesMap.put(_key, _value);
        }
    }
}
