package com.twinkle.framework.core.asm.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-28 17:01<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TypeDefine implements Define{
    /**
     * Type's Name, support GeneralContentResult<T>
     *     GeneralContentResult<T extends List>
     */
    private String name;
    /**
     * Type's class.
     */
    @NonNull
    private Class<?> typeClass;
    /**
     * Generic Type
     */
    private EnumGenericType genericType = EnumGenericType.IS;
    /**
     * the Type list for class generic.
     */
    private List<TypeDefine> genericTypeList;

    /**
     * Add generic type.
     *
     * @param _typeDefine
     */
    public void addGenericType(TypeDefine... _typeDefine) {
        if(CollectionUtils.isEmpty(this.genericTypeList)) {
            this.genericTypeList = new ArrayList<>();
        }
        if(_typeDefine == null || _typeDefine.length == 0) {
            return;
        }
        this.genericTypeList.addAll(Arrays.asList(_typeDefine));
    }
}
