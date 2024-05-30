package integration.annotationProcessors;

import integration.annotationProcessors.annotations.UserAnnotation1;
import integration.annotationProcessors.annotations.UserAnnotation2;
import integration.annotationProcessors.annotations.UserAnnotation3;
import org.est0y.honestannotations.annotationProcessors.BeanFactoryPostProcessorResolver;
import org.est0y.honestannotations.annotationsTools.AnnotationFinder;
import org.est0y.honestannotations.annotationsTools.NestedAnnotations;
import org.est0y.honestannotations.annotationsTools.OrderedHonestAnnotationsHolder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {
        AnnotationFinder.class,
        NestedAnnotations.class,
        BeanFactoryPostProcessorResolver.class,
        Handler1.class,
        Handler2.class,
        Handler3.class,
        BeanFactoryPostProcessorResolverTest.TestClass1.class,
        BeanFactoryPostProcessorResolverTest.TestClass2.class,
})
public class BeanFactoryPostProcessorResolverTest {

    @UserAnnotation1
    @UserAnnotation2
    @Component("testClass1")
    public static class TestClass1 {
    }

    @UserAnnotation1

    @Component("testClass2")
    public static class TestClass2 {

        @UserAnnotation3
        private Object field;
    }

    @Autowired
    private OrderedHonestAnnotationsHolder orderedHonestAnnotationsHolder;

    @Test
    @DirtiesContext
    void checkBeanAnnotations() {
        var testClass1Annotations = orderedHonestAnnotationsHolder.getAfterInitAnnotations("testClass1").stream().map(a -> a.annotationType().getName());
        var testClass2Annotations = orderedHonestAnnotationsHolder.getAfterInitAnnotations("testClass2").stream().map(a -> a.annotationType().getName());
        var testClass2AnnotationsBeforeInit = orderedHonestAnnotationsHolder.getBeforeInitAnnotations("testClass2").stream().map(a -> a.annotationType().getName());
        assertThat(testClass1Annotations).containsExactlyInAnyOrderElementsOf(List.of(UserAnnotation1.class.getName(), UserAnnotation2.class.getName()));
        assertThat(testClass2Annotations).containsExactlyInAnyOrderElementsOf(List.of(UserAnnotation1.class.getName()));
        assertThat(testClass2AnnotationsBeforeInit).containsExactlyInAnyOrderElementsOf(List.of(UserAnnotation3.class.getName()));
    }
}
