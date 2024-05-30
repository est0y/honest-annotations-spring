package org.est0y.honestannotations.annotations;

import org.est0y.honestannotations.handlers.AnnotationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeInitialization {
    Class<? extends AnnotationHandler> value();
}
