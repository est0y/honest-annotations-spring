package org.est0y.honestannotations.annotationsTools;

import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.NavigableSet;


@RequiredArgsConstructor
public class OrderedHonestAnnotationsHolder {

    private final Map<String, NavigableSet<Annotation>> honestAnnotationsBeforeInitialization;

    private final Map<String, NavigableSet<Annotation>> honestAnnotationsAfterInitialization;


    public NavigableSet<Annotation> getBeforeInitAnnotations(String beanName) {
        return honestAnnotationsBeforeInitialization.get(beanName);
    }

    public NavigableSet<Annotation> getAfterInitAnnotations(String beanName) {
        return honestAnnotationsAfterInitialization.get(beanName);
    }
}
