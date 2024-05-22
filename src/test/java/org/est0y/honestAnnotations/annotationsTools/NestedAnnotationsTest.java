package org.est0y.honestAnnotations.annotationsTools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.stream.Stream;

class NestedAnnotationsTest {

    private static class Handler1 {
    }

    private static class Handler2 {
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface NestedAnnotation {
        Class<?> value();
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @NestedAnnotation(Handler1.class)
    private @interface MetaAnnotation {

    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @MetaAnnotation
    @NestedAnnotation(Handler2.class)
    private @interface MetaAnnotation2 {

    }

    @Target({ElementType.TYPE,})
    @Retention(RetentionPolicy.RUNTIME)
    @MetaAnnotation2
    @MetaAnnotation
    private @interface TestAnnotation {
    }


    @TestAnnotation
    private static class AnnotatedClass {
    }

    @Test
    void getNestedAnnotation() {
        var nestedAnnotations = new NestedAnnotations();
        var annotation = AnnotatedClass.class.getAnnotation(TestAnnotation.class);
        var annotationValues = nestedAnnotations.findNestedAnnotations(annotation, NestedAnnotation.class).stream()
                .map(a -> a.value().getSimpleName());
        Assertions.assertLinesMatch(Stream.of(
                        Handler1.class.getSimpleName(),
                        Handler2.class.getSimpleName(),
                        Handler1.class.getSimpleName()
                ),
                annotationValues
        );
    }

    @Test
    void getParentsAnnotations() {
        var nestedAnnotations = new NestedAnnotations();
        var annotation = AnnotatedClass.class.getAnnotation(TestAnnotation.class);
        var annotationValues = nestedAnnotations.findParentAnnotations(annotation, NestedAnnotation.class).stream()
                .map(a -> a.annotationType().getName());
        Assertions.assertLinesMatch(Stream.of(
                MetaAnnotation.class.getName(),
                MetaAnnotation2.class.getName(),
                MetaAnnotation.class.getName()
                ),
                annotationValues
        );

    }
}