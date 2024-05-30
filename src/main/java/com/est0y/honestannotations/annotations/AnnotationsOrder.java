package com.est0y.honestannotations.annotations;

import com.est0y.honestannotations.annotationProcessors.BeanFactoryPostProcessorResolver;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to specify the order of other annotations on a bean.
 * It helps in determining the sequence in which the annotations should be processed.
 *
 * <p>The {@code order} element defines an array of annotation classes that specifies
 * the order in which the annotations should be processed. Beans annotated with
 * {@code @AnnotationsOrder} must also be annotated with {@code @Configuration}.</p>
 *
 * <p>This annotation is processed by the {@link BeanFactoryPostProcessorResolver} class.
 *
 * <p>Example usage:</p>
 *
 * <pre><code>
 * {@literal @}AnnotationsOrder(order = {FirstAnnotation.class, SecondAnnotation.class})
 * {@literal @}Configuration
 * public class MyOrderedAnnotations {
 * }
 * </code></pre>
 *
 * @see BeanFactoryPostProcessorResolver
 */

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationsOrder {
    Class<? extends Annotation>[] order();
}
