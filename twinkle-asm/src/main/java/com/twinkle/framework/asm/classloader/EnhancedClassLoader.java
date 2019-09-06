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
    private static final Map<String, ClassDescriptor> _primitives = new HashMap();

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
        if (_primitives.containsKey(_className)) {
            return getPrimitiveClass(_className, null);
        } else {
            String internalClassName = getInternalNormalizedClassName(_className);
            return internalClassName.startsWith("[") ? Class.forName(internalClassName, this.initialize, this) : super.findClass(_className);
        }
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
            if(log.isTraceEnabled()) {
                URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
                File tempFile = new File(classPath.getPath());
                JavaMemoryFileSystem.dump(_className, tempClassByteArray, ".class", tempFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this.defineClass(_className, tempClassByteArray, 0, tempClassByteArray.length);
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

            EnhancedClassLoader.ClassDescriptor tempDescriptor = (EnhancedClassLoader.ClassDescriptor)_primitives.get(_className);
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

            EnhancedClassLoader.ClassDescriptor tempDesinger = _primitives.get(_className);
            if (tempDesinger != null) {
                tempBuffer.append(tempDesinger.code);
            } else {
                tempBuffer.append("L").append(_className).append(";");
            }

            return tempBuffer.toString();
        }
    }

    public static String getNormalizedArrayElementClassName(String _className) {
        EnhancedClassLoader.ClassDescriptor tempDescriptor = _primitives.get(_className);
        return tempDescriptor != null ? tempDescriptor.code : getInternalNormalizedArrayElementClassName(_className);
    }

    public static String getNormalizedArrayClassName(String _className) {
        return "[" + getNormalizedArrayElementClassName(_className);
    }

    private static Class getPrimitiveClass(String _className, ClassLoader _classLoader) throws ClassNotFoundException {
        EnhancedClassLoader.ClassDescriptor tempDescriptor = _primitives.get(_className);
        if (_classLoader != null) {
            String tempClassName = "[" + tempDescriptor.code;
            return _classLoader.loadClass(tempClassName).getComponentType();
        } else {
            return tempDescriptor.descriptorClass;
        }
    }

    static {
        _primitives.put("void", new EnhancedClassLoader.ClassDescriptor(Void.TYPE, "V"));
        _primitives.put("boolean", new EnhancedClassLoader.ClassDescriptor(Boolean.TYPE, "Z"));
        _primitives.put("char", new EnhancedClassLoader.ClassDescriptor(Character.TYPE, "C"));
        _primitives.put("byte", new EnhancedClassLoader.ClassDescriptor(Byte.TYPE, "B"));
        _primitives.put("short", new EnhancedClassLoader.ClassDescriptor(Short.TYPE, "S"));
        _primitives.put("int", new EnhancedClassLoader.ClassDescriptor(Integer.TYPE, "I"));
        _primitives.put("float", new EnhancedClassLoader.ClassDescriptor(Float.TYPE, "F"));
        _primitives.put("long", new EnhancedClassLoader.ClassDescriptor(Long.TYPE, "J"));
        _primitives.put("double", new EnhancedClassLoader.ClassDescriptor(Double.TYPE, "D"));
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
