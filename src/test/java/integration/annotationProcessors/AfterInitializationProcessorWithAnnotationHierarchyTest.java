package integration.annotationProcessors;

import com.est0y.honestannotations.annotationProcessors.BeanFactoryPostProcessorResolver;
import com.est0y.honestannotations.annotationProcessors.SpringHandlerProcessor;
import com.est0y.honestannotations.annotations.AfterInitialization;
import com.est0y.honestannotations.annotationsTools.AnnotationFinder;
import com.est0y.honestannotations.annotationsTools.NestedAnnotations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@SpringBootTest(classes = {
        AnnotationFinder.class,
        NestedAnnotations.class,
        BeanFactoryPostProcessorResolver.class,
        SpringHandlerProcessor.class,
        Handler1.class,
        Handler2.class,
        AfterInitializationProcessorWithAnnotationHierarchyTest.TestClass.class})
@DirtiesContext
class AfterInitializationProcessorWithAnnotationHierarchyTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @AfterInitialization(Handler1.class)
    public @interface UserAnnotation1 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @UserAnnotation3
    public @interface UserAnnotation2 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @AfterInitialization(Handler2.class)
    public @interface UserAnnotation3 {
    }


    @UserAnnotation1
    @UserAnnotation2
    public static class TestClass {
    }

    @Autowired
    private Handler1 handler1;

    @Autowired
    private Handler2 handler2;


    @Test
    void testHandledBeansClassNamesContainTestClass() {
        Assertions.assertLinesMatch(List.of(TestClass.class.getName()), handler1.getHandleBeanClassName());
        Assertions.assertLinesMatch(List.of(TestClass.class.getName()), handler2.getHandleBeanClassName());
    }


}