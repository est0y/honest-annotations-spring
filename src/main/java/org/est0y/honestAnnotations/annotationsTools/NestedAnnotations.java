package org.est0y.honestAnnotations.annotationsTools;

import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class NestedAnnotations {

    public <T extends Annotation> List<T> findNestedAnnotations(Annotation annotation, Class<T> targetAnnotation) {

        return findParentAnnotations(annotation, targetAnnotation).stream().map(a -> a.annotationType()
                .getAnnotation(targetAnnotation)).toList();
    }

    public boolean hasNestedAnnotation(Annotation annotation, Class<? extends Annotation> targetAnnotation) {
        return !findNestedAnnotations(annotation, targetAnnotation).isEmpty();
    }

    public List<Annotation> findParentAnnotations(Annotation annotation, Class<? extends Annotation> targetAnnotation) {
        Annotation[] annotations = annotation.annotationType().getAnnotations();
        Set<Class<? extends Annotation>> visitedAnnotations = new HashSet<>();
        List<Annotation> parentAnnotations = new ArrayList<>();
        for (Annotation nestedAnnotation : annotations) {
            parentAnnotations.addAll(searchParentAnnotations(annotation, nestedAnnotation,
                    targetAnnotation, parentAnnotations, visitedAnnotations));
        }
        return parentAnnotations;
    }

    private List<Annotation> searchAnnotations(Annotation currentAnnotation,
                                               Class<? extends Annotation> targetAnnotation,
                                               List<Annotation> foundAnnotations,
                                               Set<Class<? extends Annotation>> visitedAnnotations) {
        Class<? extends Annotation> currentAnnotationType = currentAnnotation.annotationType();

        if (currentAnnotationType.equals(targetAnnotation)) {
            foundAnnotations.add(currentAnnotation);
        } else {
            visitedAnnotations.add(currentAnnotationType);
        }
        for (Annotation nestedAnnotation : currentAnnotation.annotationType().getAnnotations()) {
            if (visitedAnnotations.contains(nestedAnnotation.annotationType())) {
                continue;
            }
            foundAnnotations.addAll(searchAnnotations(nestedAnnotation, targetAnnotation, foundAnnotations,
                    visitedAnnotations));
        }
        return Collections.emptyList();
    }

    private List<Annotation> searchParentAnnotations(Annotation parentAnnotation,
                                                     Annotation currentAnnotation,
                                                     Class<? extends Annotation> targetAnnotation,
                                                     List<Annotation> parentAnnotations,
                                                     Set<Class<? extends Annotation>> visitedAnnotations) {
        Class<? extends Annotation> currentAnnotationType = currentAnnotation.annotationType();

        if (currentAnnotationType.equals(targetAnnotation)) {
            parentAnnotations.add(parentAnnotation);
        } else {
            visitedAnnotations.add(currentAnnotationType);
        }
        for (Annotation nestedAnnotation : currentAnnotation.annotationType().getAnnotations()) {
            if (visitedAnnotations.contains(nestedAnnotation.annotationType())) {
                continue;
            }
            parentAnnotations.addAll(searchParentAnnotations(currentAnnotation, nestedAnnotation, targetAnnotation,
                    parentAnnotations, visitedAnnotations));
        }
        return Collections.emptyList();
    }
}

