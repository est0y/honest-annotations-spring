package org.est0y.honestannotations.config;

import lombok.extern.slf4j.Slf4j;
import org.est0y.honestannotations.annotationProcessors.BeanFactoryPostProcessorResolver;
import org.est0y.honestannotations.annotationProcessors.SpringHandlerProcessor;
import org.est0y.honestannotations.annotationsTools.AnnotationFinder;
import org.est0y.honestannotations.annotationsTools.NestedAnnotations;
import org.est0y.honestannotations.annotationsTools.OrderedHonestAnnotationsHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class HonestAutoConfiguration {

     @Bean
    public AnnotationFinder annotationFinder() {
        return new AnnotationFinder();
    }

    @Bean
    public NestedAnnotations nestedAnnotations() {
        return new NestedAnnotations();
    }

    @Bean
    public BeanFactoryPostProcessorResolver beanFactoryPostProcessorResolver() {
        return new BeanFactoryPostProcessorResolver();
    }

    @Bean
    public SpringHandlerProcessor springHandlerProcessor(ApplicationContext applicationContext,
                                                         OrderedHonestAnnotationsHolder holder) {
        return new SpringHandlerProcessor(applicationContext, holder);
    }
}
