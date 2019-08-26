package com.twinkle.framework.bootstarter.config;

import com.twinkle.framework.api.data.GeneralResult;
import com.twinkle.framework.bootstarter.data.HelloRequest;
import com.twinkle.framework.connector.http.server.classloader.RestControllerClassLoader;
import com.twinkle.framework.core.context.ContextSchema;
import com.twinkle.framework.core.datastruct.descriptor.*;
import com.twinkle.framework.core.datastruct.builder.AttributeDescriptorBuilder;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.AttributeInfo;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 11:07<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
//@Configuration
public class AsmBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        ClassLoader currentLoader = ClassUtils.getDefaultClassLoader();
        RestControllerClassLoader tempLoader = new RestControllerClassLoader(currentLoader, this.getRestClassDescriptor());
        try {
            Class<?> tempClass = tempLoader.loadClass("com.twinkle.framework.bootstarter.controller.HelloWorldController");
            log.debug("The class is : []" + tempClass);

            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(tempClass);
            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

            //注册bean定义
            registry.registerBeanDefinition("hellController2", beanDefinition);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //
//        BeanClassLoader tempLoader = new BeanClassLoader(currentLoader, this.packDescriptors());
//
//        BeanFactory tempBeanFactory = new BeanFactoryImpl(tempLoader);
//        Object tempObj = tempBeanFactory.newInstance("HelloWorld");
//        log.debug("The new obj is:{}", tempObj);
//        Class<?> tempClass = tempObj.getClass();
//        log.debug("The new obj Class is:{}", tempClass);
    }

    private String[][] getAttributeDefineList(){
        String[] tempAttr1 = new String[]{"userName", "com.twinkle.framework.core.lang.StringAttribute"};
        String[] tempAttr2 = new String[]{"passWord", "com.twinkle.framework.core.lang.StringAttribute"};
        String[] tempAttr3 = new String[]{"resultData", "com.twinkle.framework.core.lang.StringAttribute"};
//        String[] tempAttr4 = new String[]{"", ""};
//        String[] tempAttr5 = new String[]{"", ""};
//        String[] tempAttr6 = new String[]{"", ""};

        return new String[][]{tempAttr1,tempAttr2,tempAttr3};//,tempAttr4,tempAttr5,tempAttr6
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private GeneralClassTypeDescriptor getRestClassDescriptor(){
        GeneralClassTypeDescriptorImpl tempDiscriptor = new GeneralClassTypeDescriptorImpl(
                "com.twinkle.framework.bootstarter.controller.HelloWorldController",
                "HelloWorldController",
                "Lcom/twinkle/framework/bootstarter/controller/HelloWorldController",
                this.getAttributeList(),
                this.getAnnotationList(),
                this.getMethodList()
        );
        return tempDiscriptor;
    }

    private Set<String> getAnnotationList(){
        Set<String> tempAnnotations = new HashSet<>(3);
        tempAnnotations.add("@org.springframework.web.bind.annotation.RestController");
        tempAnnotations.add("@lombok.extern.slf4j.Slf4j");
        tempAnnotations.add("@io.swagger.annotations.Api");
        return tempAnnotations;
    }

    private List<AttributeDescriptor> getAttributeList(){
        List<AttributeDescriptor> tempList = new ArrayList<>(2);
        Set<String> tempAnnotationList = new HashSet<>();
//        tempAnnotationList.add("@org.springframework.beans.factory.annotation.Autowired");
//        TypeDescriptor tempTypeDescriptor = TypeDescriptorImpl.builder()
//                .className(HelloWorldService.class.getName())
//                .description(Type.getDescriptor(HelloWorldService.class))
//                .name("HelloWorldService").build();
//        AttributeDescriptorImpl tempAttr = AttributeDescriptorImpl.builder()
//                .access(Opcodes.ACC_PRIVATE)
//                .annotations(tempAnnotationList)
//                .isReadOnly(false)
//                .isRequired(true)
//                .name("helloWorldService")
//                .type(tempTypeDescriptor)
////                .className(HelloWorldService.class.getName())
//                .build();
//        tempList.add(tempAttr);

//        tempAnnotationList = new HashSet<>();
//        tempTypeDescriptor = TypeDescriptorImpl.builder()
//                .className(Logger.class.getName())
//                .description(Type.getDescriptor(Logger.class))
//                .name("Logger").build();
//        tempAttr = AttributeDescriptorImpl.builder()
//                .access(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL)
//                .annotations(tempAnnotationList)
//                .isReadOnly(false)
//                .isRequired(true)
//                .name("log")
//                .type(tempTypeDescriptor)
//                .build();
//        tempList.add(tempAttr);

        return tempList;
    }

    private List<MethodTypeDescriptor> getMethodList() {
        List<MethodTypeDescriptor> tempList = new ArrayList<>();
        Set<String> tempAnnotationList = new HashSet<>();
        tempAnnotationList.add("@io.swagger.annotations.ApiOperation(value = \"Test Token\")");
        tempAnnotationList.add("@org.springframework.web.bind.annotation.RequestMapping(value = \"test/token/{_userName}\", method = org.springframework.web.bind.annotation.RequestMethod.POST)");

        TypeDescriptor tempReturn = TypeDescriptorImpl.builder()
                .className("com.twinkle.framework.api.data.GeneralResult<java.lang.String>")
                .name("GeneralResult")
                .description("Lcom/twinkle/framework/api/data.GeneralResult<Ljava/lang/String;>;")
                .build();
        MethodTypeDescriptor tempMethod = MethodTypeDescriptorImpl.builder()
                .access(Opcodes.ACC_PUBLIC)
                .annotations(tempAnnotationList)
                .name("sayHello")
//                .instructionHandler("addHelloWorldRuleChain")
                .parameterAttrs(this.packMethodParameters())
                .localParameterAttrs(this.pathMethodLocalParameters())
                .returnType(tempReturn)
                .build();
        tempList.add(tempMethod);
        return tempList;
    }

    private List<AttributeDescriptor> pathMethodLocalParameters(){
        List<AttributeDescriptor> tempResult = new ArrayList<>(2);
        AttributeInfo tempContentAttr = new AttributeInfo(
                Attribute.STRING_TYPE,
                0,
                "tempContent",
                3,
                String.class
        );
        tempResult.add(AttributeDescriptorBuilder.getMethodLocalParameter(tempContentAttr));
        AttributeInfo tempResultAttr = new AttributeInfo(
                Attribute.OBJECT_TYPE,
                0,
                "tempResult",
                4,
                GeneralResult.class.getName(),
                "Lcom/twinkle/framework/api/data.GeneralResult<Ljava/lang/String;>;"
        );
        tempResult.add(AttributeDescriptorBuilder.getMethodLocalParameter(tempResultAttr));


        return tempResult;
    }

    private List<AttributeDescriptor> packMethodParameters() {
        List<AttributeDescriptor> tempResult = new ArrayList<>(2);
        AttributeInfo tempBodyAttr = new AttributeInfo(
                Attribute.OBJECT_TYPE,
                0,
                "_request",
                1,
                HelloRequest.class
        );
        tempResult.add(AttributeDescriptorBuilder.getRequestBodyMethodParameter(tempBodyAttr));
        AttributeInfo tempUserAttr = new AttributeInfo(
                Attribute.STRING_TYPE,
                0,
                "_userName",
                2,
                String.class
        );
        tempResult.add(AttributeDescriptorBuilder.getPathVarMethodParameter(tempUserAttr));

        return tempResult;
    }
}
