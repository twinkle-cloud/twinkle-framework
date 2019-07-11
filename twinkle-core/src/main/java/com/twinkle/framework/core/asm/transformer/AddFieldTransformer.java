package com.twinkle.framework.core.asm.transformer;

import com.twinkle.framework.core.asm.data.AnnotationDefine;
import com.twinkle.framework.core.asm.data.FieldDefine;
import com.twinkle.framework.core.utils.TypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-26 21:50<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class AddFieldTransformer extends ClassTransformer {
    /**
     * Field Definition.
     */
    private FieldDefine fieldDefine;

    public AddFieldTransformer(ClassTransformer _transformer, FieldDefine _fieldDefine) {
        super(_transformer);
        this.fieldDefine = _fieldDefine;
    }

    @Override
    public void transform(ClassNode _classNode) {
        boolean isPresent = false;
        for (FieldNode tempFieldNode : _classNode.fields) {
            if (this.fieldDefine.getName().equals(tempFieldNode.name)) {
                isPresent = true;
                break;
            }
        }
        if (!isPresent) {
            FieldNode tempFieldNode = new FieldNode(
                    this.fieldDefine.getAccess(),
                    this.fieldDefine.getName(),
                    Type.getDescriptor(this.fieldDefine.getTypeDefine().getTypeClass()),
                    TypeUtil.getTypeSignature(this.fieldDefine.getTypeDefine()),
                    this.fieldDefine.getIntialValue());

            tempFieldNode.visibleAnnotations = this.getAnnotationNode();
            //Add Class Visitor.
            tempFieldNode.accept(_classNode);
//            _classNode.fields.add(tempFieldNode);
        }
        super.transform(_classNode);
    }

    /**
     * Going to Pack the Annotation Node List.
     *
     * @return
     */
    private List<AnnotationNode> getAnnotationNode() {
        if (CollectionUtils.isEmpty(this.fieldDefine.getAnnotationDefineList())) {
            return new ArrayList<>();
        }
        return this.fieldDefine.getAnnotationDefineList().stream().map(this::packAnnotationNode).collect(Collectors.toList());
    }

    /**
     * Pack the Annotation Node.
     *
     * @param _define
     * @return
     */
    private AnnotationNode packAnnotationNode(AnnotationDefine _define) {
        log.debug("Going to add field's annotation {}", _define);
        AnnotationNode tempNode = new AnnotationNode(Type.getDescriptor(_define.getAnnotationClass()));
        Map<String, Object> tempItemMap = _define.getValuesMap();

        if (tempItemMap == null || tempItemMap.size() == 0) {
            return tempNode;
        }
        List<Object> tempValuesList = new ArrayList<>();
        tempItemMap.forEach((k, v) -> {tempValuesList.add(k); tempValuesList.add(v);});
        tempNode.values = tempValuesList;
//        tempItemMap.forEach((k, v) -> tempNode.visit(k, v));
        return tempNode;
    }
}
