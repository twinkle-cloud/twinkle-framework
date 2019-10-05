package com.twinkle.framework.asm.utils;

import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.classloader.BeanClassLoader;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/5/19 5:04 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanUtil {
    /**
     * Get the StructAttribute's qualified name.
     *
     * @param _className
     * @return
     */
    public static String getStructAttributeQualifiedTypeName(String _className) {
        return _className.substring(Bean.STRUCT_ATTRIBUTE_GENERAL_PACKAGE.length()).replace('.', ':');
    }

    /**
     * Get the StructAttribute's interface name.
     *
     * @param _className
     * @return
     */
    public static String getStructAttributeInterfaceName(String _className) {
        return _className.startsWith(Bean.STRUCT_ATTRIBUTE_GENERAL_PACKAGE) ? _className : Bean.STRUCT_ATTRIBUTE_GENERAL_PACKAGE + _className.replace(':', '.');
    }

    /**
     * Return the General bean class name for struct attribute.
     *
     * @param _className
     * @return
     */
    public static String getStructAttributeGeneralClassName(String _className) {
        return _className.startsWith(Bean.STRUCT_ATTRIBUTE_GENERAL_PACKAGE) ? _className : Bean.STRUCT_ATTRIBUTE_GENERAL_PACKAGE + _className.replace(':', '.');
    }

    /**
     * Convert the type name to the general bean's interface name.
     *
     * @param _typeName
     * @return
     */
    public static String typeNameToInterfaceName(String _typeName) {
        return _typeName.startsWith(Bean.DEFAULT_PACKAGE) ? getInterfaceName(_typeName) : Bean.DEFAULT_PACKAGE + _typeName.replaceAll(Bean.LEGACY_TYPE_SEPARATOR, Bean.TYPE_SEPARATOR);
    }

    /**
     * Convert the type name to the general bean's impl class name.
     *
     * @param _typeName
     * @return
     */
    public static String typeNameToClassName(String _typeName) {
        return _typeName.startsWith(Bean.DEFAULT_PACKAGE) ? getClassName(_typeName) : Bean.DEFAULT_PACKAGE + _typeName.replaceAll(Bean.LEGACY_TYPE_SEPARATOR, Bean.TYPE_SEPARATOR) + "Impl";
    }

    /**
     * Convert the type name to the general bean's impl builder class name.
     *
     * @param _typeName
     * @return
     */
    public static String typeNameToImplBuilderClassName(String _typeName) {
        return _typeName.startsWith(Bean.DEFAULT_PACKAGE) ? BeanUtil.getImplBuilderName(_typeName) : Bean.DEFAULT_PACKAGE + _typeName.replaceAll(Bean.LEGACY_TYPE_SEPARATOR, Bean.TYPE_SEPARATOR) + "Impl$ImplBuilder";
    }

    /**
     * Convert the type name to the struct attribute bean's interface name.
     *
     * @param _typeName
     * @return
     */
    public static String structTypeNameToInterfaceName(String _typeName) {
        return _typeName.startsWith(Bean.STRUCT_ATTRIBUTE_PACKAGE) ? getInterfaceName(_typeName) : Bean.STRUCT_ATTRIBUTE_PACKAGE + _typeName.replaceAll(Bean.LEGACY_TYPE_SEPARATOR, Bean.TYPE_SEPARATOR);
    }

    /**
     * Convert the type name to the struct attribute bean's impl class name.
     *
     * @param _typeName
     * @return
     */
    public static String structTypeNameToClassName(String _typeName) {
        return _typeName.startsWith(Bean.STRUCT_ATTRIBUTE_PACKAGE) ? getClassName(_typeName) : Bean.STRUCT_ATTRIBUTE_PACKAGE + _typeName.replaceAll(Bean.LEGACY_TYPE_SEPARATOR, Bean.TYPE_SEPARATOR) + "Impl";
    }

    /**
     * Convert the type name to the struct attribute bean's impl builder class name.
     *
     * @param _typeName
     * @return
     */
    public static String structTypeNameToImplBuilderClassName(String _typeName) {
        return _typeName.startsWith(Bean.DEFAULT_PACKAGE) ? BeanUtil.getImplBuilderName(_typeName) : Bean.DEFAULT_PACKAGE + _typeName.replaceAll(Bean.LEGACY_TYPE_SEPARATOR, Bean.TYPE_SEPARATOR) + "Impl$ImplBuilder";
    }
    /**
     * Get classname for IMPL or builder class.
     *
     * @param _className
     * @return
     */
    public static String getClassName(String _className) {
        if (_className.endsWith(Bean.IMPL_SUFFIX)) {
            return _className;
        } else {
            return _className.endsWith(Bean.IMPL_BUILDER_SUFFIX) ? _className.substring(0, _className.length() - Bean.IMPL_BUILDER_SUFFIX.length()) : _className + Bean.IMPL_SUFFIX;
        }
    }

    /**
     * Get the bean's interface name with bean name.
     *
     * @param _className
     * @return
     */
    public static String getInterfaceName(String _className) {
        if (_className.endsWith(Bean.IMPL_SUFFIX)) {
            return _className.substring(0, _className.length() - Bean.IMPL_SUFFIX.length());
        } else {
            return _className.endsWith(Bean.IMPL_IMPL_BUILDER_SUFFIX) ? _className.substring(0, _className.length() - Bean.IMPL_IMPL_BUILDER_SUFFIX.length()) : _className;
        }
    }

    /**
     * Get IMPL builder name.
     *
     * @param _className
     * @return
     */
    public static String getImplBuilderName(String _className) {
        if (_className.endsWith(Bean.IMPL_BUILDER_SUFFIX)) {
            return _className;
        } else {
            return _className.endsWith(Bean.IMPL_SUFFIX) ? _className + Bean.IMPL_BUILDER_SUFFIX : _className + Bean.IMPL_IMPL_BUILDER_SUFFIX;
        }
    }
}
