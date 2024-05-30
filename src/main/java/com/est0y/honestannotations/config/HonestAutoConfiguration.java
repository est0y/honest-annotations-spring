package com.est0y.honestannotations.config;

import com.est0y.honestannotations.annotationProcessors.BeanFactoryPostProcessorResolver;
import com.est0y.honestannotations.annotationProcessors.HonestAnnotationsBeanPostProcessor;
import com.est0y.honestannotations.annotationsTools.AnnotationFinder;
import com.est0y.honestannotations.annotationsTools.NestedAnnotations;
import com.est0y.honestannotations.annotationsTools.OrderedHonestAnnotationsHolder;
import lombok.extern.slf4j.Slf4j;
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
    public HonestAnnotationsBeanPostProcessor springHandlerProcessor(ApplicationContext applicationContext,
                                                                     OrderedHonestAnnotationsHolder holder) {
        return new HonestAnnotationsBeanPostProcessor(applicationContext, holder);
    }
}
