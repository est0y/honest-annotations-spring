package org.est0y.honestAnnotations.annotationsTools;

import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.NavigableSet;


@RequiredArgsConstructor
public class OrderedHonestAnnotationsHolder {

    private final Map<String, NavigableSet<Annotation>> honestAnnotationsByBeanName;

    public NavigableSet<Annotation> getAnnotations(String beanName) {
        return honestAnnotationsByBeanName.get(beanName);
    }
}
