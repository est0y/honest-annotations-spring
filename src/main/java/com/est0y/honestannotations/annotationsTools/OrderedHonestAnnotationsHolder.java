package com.est0y.honestannotations.annotationsTools;

import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableSet;


@RequiredArgsConstructor
public class OrderedHonestAnnotationsHolder {

    private final Map<String, NavigableSet<Annotation>> honestAnnotationsBeforeInitialization;

    private final Map<String, NavigableSet<Annotation>> honestAnnotationsAfterInitialization;


    public NavigableSet<Annotation> getBeforeInitAnnotations(String beanName) {
        var annotations = honestAnnotationsBeforeInitialization.get(beanName);
        return annotations == null ? Collections.emptyNavigableSet() : annotations;
    }

    public NavigableSet<Annotation> getAfterInitAnnotations(String beanName) {
        var annotations = honestAnnotationsAfterInitialization.get(beanName);
        return annotations == null ? Collections.emptyNavigableSet() : annotations;
    }
}
