package com.est0y.honestannotations.annotations;

import com.est0y.honestannotations.handlers.AnnotationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation to specify a handler class that will be used to process the annotated bean after its initialization.
 * <p>
 * This annotation should be used in conjunction with other custom annotations that are applied to Spring beans.
 * The specified handler class should implement the {@link AnnotationHandler} interface and define the logic
 * for handling the bean after its initialization.
 * </p>
 * <p>Example usage:</p>
 * <pre>
 * &#64;Target({ElementType.TYPE})
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * &#64;AfterInitialization(UserAnnotationHandler.class)
 * public &#64;interface UserAnnotation {
 * }
 *
 * &#64;UserAnnotation
 * &#64;Component("userClass")
 * public class UserClass {
 *     public void method() {
 *         System.out.println("origin method");
 *     }
 * }
 * </pre>
 *
 * <p>The {@code UserAnnotationHandler} class would be responsible for wrapping the bean in a proxy
 * to modify its behavior.</p>
 *
 * @see AnnotationHandler
 */

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterInitialization {
    Class<? extends AnnotationHandler> value();
}
