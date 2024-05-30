package com.est0y.honestannotations.annotationProcessors;

import com.est0y.honestannotations.handlers.AnnotationHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.est0y.honestannotations.annotations.AfterInitialization;
import com.est0y.honestannotations.annotations.BeforeInitialization;
import com.est0y.honestannotations.annotationsTools.OrderedHonestAnnotationsHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.NavigableSet;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpringHandlerProcessor implements BeanPostProcessor, Ordered {

    private final ApplicationContext applicationContext;

    private final OrderedHonestAnnotationsHolder orderedHonestAnnotationsHolder;

    @Override
    @Nullable
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        NavigableSet<Annotation> honestAnnotations = orderedHonestAnnotationsHolder.getBeforeInitAnnotations(beanName);
        return handleHonestAnnotations(bean, beanName, honestAnnotations, (honestAnnotation -> {
            BeforeInitialization beforeInitialization = honestAnnotation.annotationType()
                    .getAnnotation(BeforeInitialization.class);
            return beforeInitialization.value();
        }));
    }

    @Override
    @Nullable
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        NavigableSet<Annotation> honestAnnotations = orderedHonestAnnotationsHolder.getAfterInitAnnotations(beanName);
        return handleHonestAnnotations(bean, beanName, honestAnnotations, (honestAnnotation -> {
            AfterInitialization afterInitialization = honestAnnotation.annotationType()
                    .getAnnotation(AfterInitialization.class);
            return afterInitialization.value();
        }));
    }

    private Object handleHonestAnnotations(Object bean, String beanName,
                                           NavigableSet<Annotation> honestAnnotations,
                                           Function<Annotation, Class<? extends AnnotationHandler>> function) {
        for (Annotation honestAnnotation : honestAnnotations) {
            Class<? extends AnnotationHandler> handlerClass = function.apply(honestAnnotation);
            var handler = applicationContext.getBean(handlerClass);
            bean = handler.handle(bean, beanName);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}