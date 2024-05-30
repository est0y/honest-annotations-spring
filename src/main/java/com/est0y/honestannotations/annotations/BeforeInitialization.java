package com.est0y.honestannotations.annotations;

import com.est0y.honestannotations.annotationProcessors.HonestAnnotationsBeanPostProcessor;
import com.est0y.honestannotations.handlers.AnnotationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation to specify a handler class that will be used to process the annotated bean before its initialization.
 * <p>
 * This annotation should be placed inside other custom annotations that are applied to Spring beans.
 * The specified handler class should implement the {@link AnnotationHandler} interface and define the logic
 * for handling the bean before its initialization.
 * </p>
 * <p>Example usage:</p>
 * <pre><code>
 * &#64;Target({ElementType.TYPE})
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * &#64;BeforeInitialization(UserAnnotationHandler.class)
 * public &#64;interface UserAnnotation {
 * }
 *
 * &#64;UserAnnotation
 * &#64;Component
 * public class UserClass {
 *     public void method() {
 *         System.out.println("origin method");
 *     }
 * }
 *
 * &#64;Component
 * public class UserAnnotationHandler implements {@link AnnotationHandler} {
 *     &#64;Override
 *     public Object handle(Object bean, String beanName) {
 *          //handle logic here
 *          return bean;
 *      }
 * }
 * </code></pre>
 *
 * <p>The handler specified in the {@code value} of the {@link BeforeInitialization} annotation
 * will be invoked in the {@code postProcessBeforeInitialization} method of the
 * {@link HonestAnnotationsBeanPostProcessor}.</p>
 *
 * @see HonestAnnotationsBeanPostProcessor
 * @see AnnotationHandler
 * @see AfterInitialization
 */

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeInitialization {
    Class<? extends AnnotationHandler> value();
}
