package com.twinkle.framework.core.asm.transformer;

import com.twinkle.framework.core.asm.data.AnnotationDefine;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-03 22:28<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class AddAnnotationTransformer extends ClassTransformer {
    private AnnotationDefine annotationDefine;

    public AddAnnotationTransformer(ClassTransformer _transformer, AnnotationDefine _annotationDefine) {
        super(_transformer);
        this.annotationDefine = _annotationDefine;
    }
    @Override
    public void transform(ClassNode _classNode) {
        boolean isPresent = false;
        for (AnnotationNode tempAnnotationNode : _classNode.visibleAnnotations) {
            if (Type.getDescriptor(this.annotationDefine.getAnnotationClass()).equals(tempAnnotationNode.desc)) {
                isPresent = true;
                break;
            }
        }
        if (!isPresent) {

        }
    }
}
