package com.twinkle.framework.connector.http.server.designer;

import com.twinkle.framework.asm.builder.LogAttributeDefBuilder;
import com.twinkle.framework.asm.define.AttributeDef;
import com.twinkle.framework.asm.define.BeanTypeDef;
import com.twinkle.framework.asm.define.GeneralClassTypeDef;
import com.twinkle.framework.asm.designer.AbstractGeneralClassDesigner;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.*;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-15 15:28<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class RestControllerClassDesigner extends AbstractGeneralClassDesigner {
    public RestControllerClassDesigner(String _className, GeneralClassTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
    }

    @Override
    protected void addClassDefinition(ClassVisitor _visitor, String _className, String _superName, List<AttributeDef> _attrDefList, BeanTypeDef _beanTypeDef) {
        List<AttributeDef> tempAttrList = _attrDefList;
        tempAttrList.add(LogAttributeDefBuilder.getAttributeDef());
        super.addClassDefinition(_visitor, _className, _superName, tempAttrList, _beanTypeDef);
        this.addMethodsDefinition(_visitor, _className, ((GeneralClassTypeDef)_beanTypeDef).getMethods());
    }
}
