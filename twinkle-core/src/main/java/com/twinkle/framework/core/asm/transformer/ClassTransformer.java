package com.twinkle.framework.core.asm.transformer;

import org.objectweb.asm.tree.ClassNode;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-26 17:22<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ClassTransformer {
    protected ClassTransformer classTransformer;
    public ClassTransformer(ClassTransformer _transformer) {
        this.classTransformer = _transformer;
    }
    public void transform(ClassNode _node) {
        if (classTransformer != null) {
            classTransformer.transform(_node);
        }
    }
}
