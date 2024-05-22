package org.est0y.honestAnnotations.annotationsTools;

import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class AnnotationOrderComparator implements Comparator<java.lang.annotation.Annotation> {

    private final List<Class<? extends Annotation>> order;

    @Override
    public int compare(Annotation a1, Annotation a2) {
        int index1 = order.indexOf(a1.annotationType());
        int index2 = order.indexOf(a2.annotationType());
        index1 = index1 == -1 ? Integer.MAX_VALUE : index1;
        index2 = index2 == -1 ? Integer.MAX_VALUE : index2;
        int result = Integer.compare(index1, index2);
        return result == 0 ? 1 : result;
    }
}
