package integration.annotationProcessors.annotations;

import integration.annotationProcessors.Handler3;
import org.est0y.honestAnnotations.annotations.BeforeInitialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@BeforeInitialization(Handler3.class)
public @interface UserAnnotation3 {
}