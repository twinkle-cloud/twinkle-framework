package com.twinkle.framework.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Function: Abstract Routing Data Source. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class DynamicDataSourceClassResolver {
    private static boolean mpEnabled = false;
//    private static Field mapperInterfaceField;
//
//    static {
//        Class<?> proxyClass = null;
//        try {
//            proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.MybatisMapperProxy");
//        } catch (ClassNotFoundException e1) {
//            try {
//                proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.PageMapperProxy");
//            } catch (ClassNotFoundException e2) {
//                try {
//                    proxyClass = Class.forName("org.apache.ibatis.binding.MapperProxy");
//                } catch (ClassNotFoundException e3) {
//                }
//            }
//        }
//        if (proxyClass != null) {
//            try {
//                mapperInterfaceField = proxyClass.getDeclaredField("mapperInterface");
//                mapperInterfaceField.setAccessible(true);
//                mpEnabled = true;
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public Class<?> targetClass(MethodInvocation _invocation) throws IllegalAccessException {
//        if (mpEnabled) {
//            Object tempTargetObj = _invocation.getThis();
//            Class<?> tempTargetClass = tempTargetObj.getClass();
//            return Proxy.isProxyClass(tempTargetClass) ? (Class) mapperInterfaceField
//                    .get(Proxy.getInvocationHandler(tempTargetObj)) : tempTargetClass;
//        }
        return _invocation.getMethod().getDeclaringClass();
    }
}
