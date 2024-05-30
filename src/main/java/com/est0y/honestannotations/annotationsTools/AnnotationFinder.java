package com.est0y.honestannotations.annotationsTools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AnnotationFinder {

    public List<Annotation> getAllAnnotations(Class<?> beanClass) {
        var beanAnnotations = new ArrayList<>(Arrays.stream(beanClass.getAnnotations()).toList());
        beanAnnotations.addAll(geMethodsAnnotations(beanClass));
        beanAnnotations.addAll(getFieldsAnnotations(beanClass));
        return beanAnnotations;
    }

    private List<Annotation> getFieldsAnnotations(Class<?> beanClass) {
        List<Annotation> annotations = new ArrayList<>();
        var fields = beanClass.getDeclaredFields();
        for (var field : fields) {
            annotations.addAll(Arrays.stream(field.getAnnotations()).toList());
        }
        return annotations;
    }

    private List<Annotation> geMethodsAnnotations(Class<?> beanClass) {
        List<Annotation> annotations = new ArrayList<>();
        var methods = beanClass.getDeclaredMethods();
        for (var method : methods) {
            annotations.addAll(Arrays.stream(method.getAnnotations()).toList());
        }
        return annotations;
    }

    private List<Annotation> getMethodArgsAnnotation(Class<?> beanClass) {
        List<Annotation> annotations = new ArrayList<>();
        var methods = beanClass.getDeclaredMethods();
        for (var method : methods) {
            var parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                annotations.addAll(List.of(parameter.getAnnotations()));
            }
        }
        return annotations;
    }
}
