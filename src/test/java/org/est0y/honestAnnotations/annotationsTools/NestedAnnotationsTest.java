package org.est0y.honestAnnotations.annotationsTools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.stream.Stream;

class NestedAnnotationsTest {
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface NestedAnnotation {
        Class<?> value();
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @NestedAnnotation(MetaAnnotation.class)
    private @interface MetaAnnotation {

    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @MetaAnnotation
    @NestedAnnotation(MetaAnnotation2.class)
    private @interface MetaAnnotation2 {

    }

    @Target({ElementType.TYPE,})
    @Retention(RetentionPolicy.RUNTIME)
    @MetaAnnotation2
    private @interface TestAnnotation {

    }


    @TestAnnotation
    private static class AnnotatedClass {
    }

    @Test
    void getNestedAnnotation() {
        var nestedAnnotations = new NestedAnnotations();
        var annotation = AnnotatedClass.class.getAnnotation(TestAnnotation.class);
        var annotationValues = nestedAnnotations.getNestedAnnotation(annotation, NestedAnnotation.class).stream()
                .map(a -> a.value().getSimpleName());
        Assertions.assertLinesMatch(Stream.of(MetaAnnotation.class.getSimpleName(), MetaAnnotation2.class.getSimpleName()), annotationValues);
    }
}