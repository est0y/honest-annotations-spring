package integration.annotationProcessors.annotations;

import integration.annotationProcessors.Handler2;
import org.est0y.honestAnnotations.annotations.AfterInitialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AfterInitialization(Handler2.class)
public @interface UserAnnotation2 {
}