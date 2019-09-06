package com.twinkle.framework.asm;

import com.twinkle.framework.asm.descriptor.TypeDescriptors;
import com.twinkle.framework.asm.serialize.SerializerFactory;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 11:47<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BeanFactory {
    /**
     * Build an instance with given instance's class name.
     *
     * @param _className
     * @param <T>
     * @return
     */
    <T extends Bean> T newInstance(String _className);

    /**
     * Build an instance with given class.
     *
     * @param _class
     * @param <T>
     * @return
     */
    <T extends Bean> T newInstance(Class<T> _class);

    /**
     * Build an instance array with given class name and array length.
     *
     * @param _className
     * @param _length
     * @param <T>
     * @return
     */
    <T extends Bean> T[] newArray(String _className, int _length);

    /**
     * Build an instance array with given class and array length.
     *
     * @param _class
     * @param _length
     * @param <T>
     * @return
     */
    <T extends Bean> T[] newArray(Class<T> _class, int _length);

    /**
     * Try to get bean's type with given class.
     *
     * @param _class
     * @param <T>
     * @return
     */
    <T extends Bean> String getBeanType(Class<T> _class);

    /**
     * Try to get bean's interface with given interface name.
     *
     * @param _interfaceName
     * @param <T>
     * @return
     */
    <T extends Bean> Class<T> getBeanInterface(String _interfaceName);

    /**
     * Try to get Bean's class with given class name.
     *
     * @param _className
     * @param <T>
     * @return
     */
    <T extends Bean> Class<T> getBeanClass(String _className);

    /**
     * Get bean's class loader.
     *
     * @return
     */
    ClassLoader getBeanClassLoader();

    /**
     * Get the type descriptors that will be used to build the bean instance.
     *
     * @return
     */
    TypeDescriptors getTypeDescriptors();

    /**
     * Get serializer factory with given factory name.
     *
     * @param _factoryName
     * @return
     */
    SerializerFactory getSerializerFactory(String _factoryName);

    /**
     * Get default serializer factory.
     *
     * @return
     */
    SerializerFactory getDefaultSerializerFactory();
}
