package com.twinkle.framework.asm.classloader;

import com.twinkle.framework.asm.compiler.JavaMemoryFileSystem;
import com.twinkle.framework.asm.designer.ClassDesigner;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 23:06<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class EnhancedClassLoader extends ClassLoader{
    private final boolean initialize;
    private static final Map<String, ClassDescriptor> PRIMITIVES = new HashMap<>(9);
    private static final Map<String, Class<?>> BUILT_CLASS_MAP = new HashMap<>(128);

    static {
        PRIMITIVES.put("void", new ClassDescriptor(Void.TYPE, "V"));
        PRIMITIVES.put("boolean", new ClassDescriptor(Boolean.TYPE, "Z"));
        PRIMITIVES.put("char", new ClassDescriptor(Character.TYPE, "C"));
        PRIMITIVES.put("byte", new ClassDescriptor(Byte.TYPE, "B"));
        PRIMITIVES.put("short", new ClassDescriptor(Short.TYPE, "S"));
        PRIMITIVES.put("int", new ClassDescriptor(Integer.TYPE, "I"));
        PRIMITIVES.put("float", new ClassDescriptor(Float.TYPE, "F"));
        PRIMITIVES.put("long", new ClassDescriptor(Long.TYPE, "J"));
        PRIMITIVES.put("double", new ClassDescriptor(Double.TYPE, "D"));
    }

    protected EnhancedClassLoader(boolean _isInitialize, ClassLoader _parentLoader) {
        super(_parentLoader);
        this.initialize = _isInitialize;
    }

    protected EnhancedClassLoader(ClassLoader _parentLoader) {
        super(_parentLoader);
        this.initialize = true;
    }

    protected EnhancedClassLoader() {
        this.initialize = true;
    }

    @Override
    protected Class<?> findClass(String _className) throws ClassNotFoundException {
        if (PRIMITIVES.containsKey(_className)) {
            return getPrimitiveClass(_className, null);
        }
        Class<?> tempClass = BUILT_CLASS_MAP.get(_className);
        if (tempClass != null) {
            return tempClass;
        }
        String internalClassName = getInternalNormalizedClassName(_className);
        return internalClassName.startsWith("[") ? Class.forName(internalClassName, this.initialize, this) : super.findClass(_className);
    }

    @Override
    public InputStream getResourceAsStream(String _resourceName) {
        if (_resourceName.endsWith(".class")) {
            String tempName = _resourceName.substring(0, _resourceName.length() - ".class".length());
            tempName = tempName.replace("/", ".");

            try {
                this.loadClass(tempName);
                byte[] tempByteArray = JavaMemoryFileSystem.instance().getBytecode(tempName);
                if (tempByteArray != null) {
                    return new ByteArrayInputStream(tempByteArray);
                }
            } catch (ClassNotFoundException e) {
            }
        }

        return super.getResourceAsStream(_resourceName);
    }

    protected final Class<?> defineClass(String _className, ClassDesigner _designer) throws ClassFormatError {
        byte[] tempClassByteArray = this.designClass(_designer);

        try {
            JavaMemoryFileSystem.instance().addBytecode(_className, tempClassByteArray);
            //Please be careful to enable the dump class file.
            //Because the AppClassLoader will scan the path and
            //load the class file again, then will cause some components
            //reflection method failed.
            if(log.isTraceEnabled()) {
                URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
                File tempFile = new File(classPath.getPath());
                JavaMemoryFileSystem.dump(_className, tempClassByteArray, ".class", tempFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Class<?> tempDefinedClass = this.defineClass(_className, tempClassByteArray, 0, tempClassByteArray.length);
        //Add the new class into the built class map.
        BUILT_CLASS_MAP.put(_className, tempDefinedClass);
        return tempDefinedClass;
    }

    protected byte[] designClass(ClassDesigner _designer) {
        if (log.isTraceEnabled()) {
            StringWriter tempWriter = (new StringWriter()).append("// class name ").append(_designer.getCanonicalClassName()).append(String.format("%n"));
            byte[] tempByteArray = _designer.toByteArray(false, new PrintWriter(tempWriter));
            log.info(tempWriter.toString());
            return tempByteArray;
        } else {
            return _designer.toByteArray();
        }
    }

    /**
     * Get Class's Internal normalized class name.
     *
     * @param _className
     * @return
     */
    public static String getInternalNormalizedClassName(String _className) {
        if (!_className.endsWith("[]")) {
            return _className;
        } else {
            StringBuilder tempBuilder = new StringBuilder();

            do {
                _className = _className.substring(0, _className.length() - 2);
                tempBuilder.append("[");
            } while(_className.endsWith("[]"));

            ClassDescriptor tempDescriptor = PRIMITIVES.get(_className);
            if (tempDescriptor != null) {
                tempBuilder.append(tempDescriptor.code);
            } else {
                tempBuilder.append("L").append(_className).append(";");
            }

            return tempBuilder.toString();
        }
    }

    /**
     * Get the class's internal
     *
     * @param _className
     * @return
     */
    private static String getInternalNormalizedArrayElementClassName(String _className) {
        if (_className.startsWith("[")) {
            return _className;
        } else {
            StringBuffer tempBuffer = new StringBuffer();

            while(_className.endsWith("[]")) {
                _className = _className.substring(0, _className.length() - 2);
                tempBuffer.append("[");
            }

            ClassDescriptor tempDesigner = PRIMITIVES.get(_className);
            if (tempDesigner != null) {
                tempBuffer.append(tempDesigner.code);
            } else {
                tempBuffer.append("L").append(_className).append(";");
            }

            return tempBuffer.toString();
        }
    }

    public static String getNormalizedArrayElementClassName(String _className) {
        ClassDescriptor tempDescriptor = PRIMITIVES.get(_className);
        return tempDescriptor != null ? tempDescriptor.code : getInternalNormalizedArrayElementClassName(_className);
    }

    public static String getNormalizedArrayClassName(String _className) {
        return "[" + getNormalizedArrayElementClassName(_className);
    }

    private static Class getPrimitiveClass(String _className, ClassLoader _classLoader) throws ClassNotFoundException {
        ClassDescriptor tempDescriptor = PRIMITIVES.get(_className);
        if (_classLoader != null) {
            String tempClassName = "[" + tempDescriptor.code;
            return _classLoader.loadClass(tempClassName).getComponentType();
        } else {
            return tempDescriptor.descriptorClass;
        }
    }

    private static class ClassDescriptor {
        public Class descriptorClass;
        public String code;

        public ClassDescriptor(Class _class, String _code) {
            this.descriptorClass = _class;
            this.code = _code;
        }
    }
}
