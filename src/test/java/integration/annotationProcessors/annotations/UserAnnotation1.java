package integration.annotationProcessors.annotations;

import integration.annotationProcessors.Handler1;
import org.est0y.honestAnnotations.annotations.AfterInitialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AfterInitialization(Handler1.class)
public @interface UserAnnotation1 {
}
