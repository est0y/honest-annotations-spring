package org.est0y.honestAnnotations.annotationProcessors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.est0y.honestAnnotations.annotations.SpringHandler;
import org.est0y.honestAnnotations.annotationsTools.OrderedHonestAnnotationsHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.NavigableSet;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpringHandlerProcessor implements BeanPostProcessor, Ordered {

    private final ApplicationContext applicationContext;

    private final OrderedHonestAnnotationsHolder orderedHonestAnnotationsHolder;

    @Override
    @Nullable
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return bean;
    }

    @Override
    @Nullable
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        NavigableSet<Annotation> honestAnnotations = orderedHonestAnnotationsHolder.getAnnotations(beanName);
        if (honestAnnotations == null) {
            return bean;
        }
        if (honestAnnotations.isEmpty()) {
            return bean;
        }
        for (Annotation honestAnnotation : honestAnnotations) {
            SpringHandler springHandler = honestAnnotation.annotationType().getAnnotation(SpringHandler.class);
            var handler = applicationContext.getBean(springHandler.value());
            bean = handler.handle(bean, beanName);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}