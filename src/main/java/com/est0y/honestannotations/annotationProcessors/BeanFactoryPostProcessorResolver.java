package com.est0y.honestannotations.annotationProcessors;

import com.est0y.honestannotations.annotations.AfterInitialization;
import com.est0y.honestannotations.annotations.AnnotationsOrder;
import com.est0y.honestannotations.annotations.BeforeInitialization;
import com.est0y.honestannotations.annotationsTools.AnnotationFinder;
import com.est0y.honestannotations.annotationsTools.NestedAnnotations;
import com.est0y.honestannotations.annotationsTools.OrderedHonestAnnotationsHolder;
import lombok.extern.slf4j.Slf4j;
import com.est0y.honestannotations.annotationsTools.AnnotationOrderComparator;
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
/**
 * This class implements {@link BeanFactoryPostProcessor} and is responsible for creating and registering
 * an {@link OrderedHonestAnnotationsHolder} in the application context. It processes annotations on beans
 * to categorize them into before and after initialization groups.
 *
 * <p>It uses {@link AnnotationFinder} to locate all annotations on beans and {@link NestedAnnotations}
 * to handle nested annotations. The annotations are ordered based on custom logic defined in {@link AnnotationsOrder}.</p>
 *
 * <p>This class processes {@link AnnotationsOrder} annotations to determine the order of annotations and sorts
 * the annotations according to the order specified in the {@link AnnotationsOrder} values.</p>
 */

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
        Map<String, NavigableSet<Annotation>> afterInitAnnotationsByBeanName = new HashMap<>();
        Map<String, NavigableSet<Annotation>> beforeInitAnnotationsByBeanName = new HashMap<>();
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            Class<?> beanClass = beanFactory.getType(beanName);
            if (beanClass != null) {
                List<Annotation> beanAnnotations = annotationFinder.getAllAnnotations(beanClass);
                handleAnnotations(beanName, beanAnnotations, comparator, beforeInitAnnotationsByBeanName,
                        BeforeInitialization.class);
                handleAnnotations(beanName, beanAnnotations, comparator, afterInitAnnotationsByBeanName,
                        AfterInitialization.class);
            }
        }
        log.info(afterInitAnnotationsByBeanName.toString());
        log.info(beforeInitAnnotationsByBeanName.toString());
        beanFactory.registerSingleton("orderedHonestAnnotationsHolder",
                new OrderedHonestAnnotationsHolder(beforeInitAnnotationsByBeanName,afterInitAnnotationsByBeanName));
    }

    private void handleAnnotations(String beanName,
                                   List<Annotation> beanAnnotations,
                                   Comparator<Annotation> comparator,
                                   Map<String, NavigableSet<Annotation>> honestAnnotationsByBeanName,
                                    Class<?extends Annotation> annotationType) {
        Set<Class<? extends Annotation>> processedAnnotationTypes = new HashSet<>();
        for (Annotation beanAnnotation : beanAnnotations) {
            if (processedAnnotationTypes.contains(beanAnnotation.annotationType())) {
                continue;
            }
            List<Annotation> parentAnnotations = nestedAnnotations
                    .findParentAnnotations(beanAnnotation, annotationType);
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
