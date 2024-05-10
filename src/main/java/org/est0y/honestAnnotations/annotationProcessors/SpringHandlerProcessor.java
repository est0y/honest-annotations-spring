package org.est0y.honestAnnotations.annotationProcessors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.est0y.honestAnnotations.annotations.SpringHandler;
import org.est0y.honestAnnotations.annotationsTools.AnnotationFinder;
import org.est0y.honestAnnotations.annotationsTools.NestedAnnotations;
import org.est0y.honestAnnotations.exceptions.BeanNotFound;
import org.est0y.honestAnnotations.handlers.AnnotationHandler;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpringHandlerProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    private final NestedAnnotations nestedAnnotations;

    private final AnnotationFinder annotationFinder;

    private final Map<String, List<String>> annotationHandlers = new HashMap<>();


    @Override
    @Nullable
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        var beanAnnotations = annotationFinder.getAllAnnotations(bean);

        for (var annotation : beanAnnotations) {
            var nestedAnnotationsList = nestedAnnotations.getNestedAnnotation(annotation, SpringHandler.class);
            if (!nestedAnnotationsList.isEmpty()) {
                annotationHandlers.putIfAbsent(beanName, new ArrayList<>());
            }
            for (SpringHandler springHandler : nestedAnnotationsList) {
                Class<? extends AnnotationHandler> handlerClass = springHandler.value();
                String handleBeanName;
                try {
                    handleBeanName = applicationContext.getBeanNamesForType(handlerClass)[0];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new BeanNotFound(String.format("with class %s", handlerClass.getName()), e);
                }
                annotationHandlers.get(beanName).add(handleBeanName);
            }
        }
        return bean;
    }

    @Override
    @Nullable
    public Object postProcessAfterInitialization(@NonNull Object bean,@NonNull  String beanName) throws BeansException {
        var handlersBeanNames = annotationHandlers.get(beanName);
        if (handlersBeanNames != null) {
            for (String handlerBeanName : handlersBeanNames) {
                var handler = applicationContext.getBean(handlerBeanName);
                try {
                    log.info(Arrays.stream(handler.getClass().getMethods()).map(Method::getName).toList().toString());
                    var method = handler.getClass().getMethod("handle", Object.class, String.class);
                    bean = method.invoke(handler, bean, beanName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
}