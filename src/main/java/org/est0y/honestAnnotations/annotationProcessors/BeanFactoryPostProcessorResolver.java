package org.est0y.honestAnnotations.annotationProcessors;

import lombok.extern.slf4j.Slf4j;
import org.est0y.honestAnnotations.annotations.AnnotationsOrder;
import org.est0y.honestAnnotations.annotations.AfterInitialization;
import org.est0y.honestAnnotations.annotationsTools.AnnotationFinder;
import org.est0y.honestAnnotations.annotationsTools.AnnotationOrderComparator;
import org.est0y.honestAnnotations.annotationsTools.NestedAnnotations;
import org.est0y.honestAnnotations.annotationsTools.OrderedHonestAnnotationsHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

@Component
@Slf4j
public class BeanFactoryPostProcessorResolver implements BeanFactoryPostProcessor {

    private NestedAnnotations nestedAnnotations;


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        AnnotationFinder annotationFinder = beanFactory.getBean(AnnotationFinder.class);
        nestedAnnotations = beanFactory.getBean(NestedAnnotations.class);
        List<Class<? extends Annotation>> orderAnnotations = getAnnotationOrder(beanFactory);
        var comparator = new AnnotationOrderComparator(orderAnnotations);
        Map<String, NavigableSet<Annotation>> honestAnnotationsByBeanName = new HashMap<>();
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            Class<?> beanClass = beanFactory.getType(beanName);
            if (beanClass != null) {
                List<Annotation> beanAnnotations = annotationFinder.getAllAnnotations(beanClass);
                handleAnnotations(beanName, beanAnnotations, comparator, honestAnnotationsByBeanName);
            }
        }
        log.info(honestAnnotationsByBeanName.toString());
        beanFactory.registerSingleton("orderedHonestAnnotationsHolder",
                new OrderedHonestAnnotationsHolder(honestAnnotationsByBeanName));
    }

    private void handleAnnotations(String beanName,
                                   List<Annotation> beanAnnotations,
                                   Comparator<Annotation> comparator,
                                   Map<String, NavigableSet<Annotation>> honestAnnotationsByBeanName) {
        Set<Class<? extends Annotation>> processedAnnotationTypes = new HashSet<>();
        for (Annotation beanAnnotation : beanAnnotations) {
            if (processedAnnotationTypes.contains(beanAnnotation.annotationType())) {
                continue;
            }
            List<Annotation> parentAnnotations = nestedAnnotations
                    .findParentAnnotations(beanAnnotation, AfterInitialization.class);
            if (!parentAnnotations.isEmpty()) {
                honestAnnotationsByBeanName.putIfAbsent(beanName, new TreeSet<>(comparator));
                Set<Annotation> annotations = honestAnnotationsByBeanName.get(beanName);
                for (Annotation parentAnnotation : parentAnnotations) {
                    processedAnnotationTypes.add(parentAnnotation.annotationType());
                    annotations.add(parentAnnotation);
                }
            }
        }
    }

    private List<Class<? extends Annotation>> getAnnotationOrder(ConfigurableListableBeanFactory beanFactory) {
        var beanTypes = Arrays.stream(beanFactory.getBeanNamesForAnnotation(AnnotationsOrder.class))
                .map(beanFactory::getType).toList();
        List<Class<? extends Annotation>> orderAnnotations = new ArrayList<>();
        for (Class<?> beanType : beanTypes) {
            orderResolve(beanType, orderAnnotations);
        }
        return orderAnnotations;
    }

    private void orderResolve(Class<?> beanClass, List<Class<? extends Annotation>> orderAnnotations) {
        if (!beanClass.isAnnotationPresent(Configuration.class)) {
            throw new IllegalStateException("@AnnotationsOrder without @Configuration");
        }
        AnnotationsOrder annotationsOrder = beanClass.getAnnotation(AnnotationsOrder.class);
        boolean hasDuplicates = Arrays.stream(annotationsOrder.order())
                .distinct().count() != annotationsOrder.order().length;
        if (hasDuplicates) {
            throw new IllegalStateException("@AnnotationsOrder has duplicates in class " + beanClass.getName());
        }
        List<Class<? extends Annotation>> orderList = Arrays.stream(annotationsOrder.order()).toList();
        int expectedSize = orderList.size() + orderAnnotations.size();
        orderAnnotations.addAll(orderList);
        if (orderAnnotations.stream().distinct().count() != expectedSize) {
            throw new IllegalStateException("Conflicting @AnnotationsOrder in class " + beanClass.getName());
        }
    }

}
