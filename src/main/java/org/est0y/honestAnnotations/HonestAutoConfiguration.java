package org.est0y.honestAnnotations;

import lombok.extern.slf4j.Slf4j;
import org.est0y.honestAnnotations.annotationProcessors.BeanFactoryPostProcessorResolver;
import org.est0y.honestAnnotations.annotationProcessors.SpringHandlerProcessor;
import org.est0y.honestAnnotations.annotationsTools.AnnotationFinder;
import org.est0y.honestAnnotations.annotationsTools.NestedAnnotations;
import org.est0y.honestAnnotations.annotationsTools.OrderedHonestAnnotationsHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class HonestAutoConfiguration {

     @Bean
     @ConditionalOnMissingBean
    public AnnotationFinder annotationFinder() {
        return new AnnotationFinder();
    }

    @Bean
    @ConditionalOnMissingBean
    public NestedAnnotations nestedAnnotations() {
        return new NestedAnnotations();
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanFactoryPostProcessorResolver beanFactoryPostProcessorResolver() {
        return new BeanFactoryPostProcessorResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringHandlerProcessor springHandlerProcessor(ApplicationContext applicationContext,
                                                         OrderedHonestAnnotationsHolder holder) {
        return new SpringHandlerProcessor(applicationContext, holder);
    }
}
