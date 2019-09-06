package com.twinkle.framework.asm.factory;

import com.twinkle.framework.asm.classloader.EnhancedClassLoader;
import com.twinkle.framework.asm.designer.ClassDesigner;
import com.twinkle.framework.asm.designer.InstanceBuilderDesigner;
import com.twinkle.framework.asm.builder.InstanceBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 16:08<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class InstanceBuilderFactory {
    private static final String PACKAGE_SEPARATOR = ".";
    private static final String BUILDER_SUFFIX = "$InstanceBuilder";
    private final String targetPackage;
    private final InstanceBuilderFactory.InstanceBuilderClassLoader loader;

    public InstanceBuilderFactory(Package _package, ClassLoader _classLoader) {
        if (_package == null) {
            _package = this.getClass().getPackage();
        }

        this.targetPackage = _package.getName();
        this.loader = new InstanceBuilderFactory.InstanceBuilderClassLoader(_classLoader);
    }

    protected String getBuilderClassName(String _builderClassName) {
        return this.targetPackage + PACKAGE_SEPARATOR + _builderClassName.replace('.', '_') + BUILDER_SUFFIX;
    }

    public <I> InstanceBuilder<I> newInstanceBuilder(Class<? extends InstanceBuilder> _builderClass, Class<? extends I> _interfaceClass, String _interfaceClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String tempClassName = this.getBuilderClassName(_interfaceClassName);
        ClassDesigner tempClassDesigner = this.loader.designers().get(tempClassName);
        if (tempClassDesigner == null) {
            InstanceBuilderDesigner tempInsBuilderDesigner = new InstanceBuilderDesigner(tempClassName, _builderClass, _interfaceClass, _interfaceClassName);
            this.loader.designers().put(tempClassName, tempInsBuilderDesigner);
        }

        return (InstanceBuilder)this.loader.loadClass(tempClassName).newInstance();
    }

    private static class InstanceBuilderClassLoader extends EnhancedClassLoader {
        private final Map<String, ClassDesigner> designerMap = new HashMap();

        public InstanceBuilderClassLoader(ClassLoader _classLoader) {
            super(_classLoader);
        }

        Map<String, ClassDesigner> designers() {
            return this.designerMap;
        }

        @Override
        protected Class<?> findClass(String _className) throws ClassNotFoundException {
            if (_className != null) {
                ClassDesigner tempClassDesigner = this.designerMap.get(_className);
                if (tempClassDesigner != null) {
                    return this.defineClass(_className, tempClassDesigner);
                }
            }

            return super.findClass(_className);
        }
    }
}
