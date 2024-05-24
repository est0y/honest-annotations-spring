package org.est0y.honestAnnotations.annotations;

import org.est0y.honestAnnotations.handlers.AnnotationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeInitialization {
    Class<? extends AnnotationHandler> value();
}
