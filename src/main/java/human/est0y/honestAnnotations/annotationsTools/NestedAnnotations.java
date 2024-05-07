package human.est0y.honestAnnotations.annotationsTools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class NestedAnnotations {

    public <T extends Annotation> List<T> getNestedAnnotation(Annotation annotation,
                                                              Class<T> expectedAnnotation) {
        var annotations = annotation.annotationType().getAnnotations();
        Set<Class<? extends Annotation>> visitedAnnotations = new HashSet<>();
        var result = new ArrayList<Annotation>();
        for (var nestedAnnotation : annotations) {
            result.addAll(isPresent(nestedAnnotation, expectedAnnotation, result, visitedAnnotations));
        }
        return (List<T>) result;
    }


    private List<Annotation> isPresent(Annotation annotation,
                                       Class<? extends Annotation> expectedAnnotation,
                                       List<Annotation> result,
                                       Set<Class<? extends Annotation>> visitedAnnotations) {
        Class<? extends Annotation> annotationType = annotation.annotationType();

        if (annotationType.equals(expectedAnnotation)) {
            result.add(annotation);
        } else {
            visitedAnnotations.add(annotationType);
        }
        for (var nestedAnnotation : annotation.annotationType().getAnnotations()) {
            if (visitedAnnotations.contains(nestedAnnotation.annotationType())) {
                continue;
            }
            result.addAll(isPresent(nestedAnnotation, expectedAnnotation, result, visitedAnnotations));
        }
        return Collections.emptyList();
    }
}
