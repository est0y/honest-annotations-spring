package org.est0y.honestannotations;

import lombok.extern.slf4j.Slf4j;
import org.est0y.honestannotations.annotationProcessors.BeanFactoryPostProcessorResolver;
import org.est0y.honestannotations.annotationProcessors.SpringHandlerProcessor;
import org.est0y.honestannotations.annotationsTools.AnnotationFinder;
import org.est0y.honestannotations.annotationsTools.NestedAnnotations;
import org.est0y.honestannotations.annotationsTools.OrderedHonestAnnotationsHolder;
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
