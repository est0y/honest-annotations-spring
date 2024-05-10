package org.est0y.honestAnnotations.annotationsTools;

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

    public List<Annotation> getAllAnnotations(Object bean) {
        var beanAnnotations = new ArrayList<>(Arrays.stream(bean.getClass().getAnnotations()).toList());
        beanAnnotations.addAll(geMethodsAnnotations(bean));
        beanAnnotations.addAll(getFieldsAnnotations(bean));
        return beanAnnotations;
    }

    private List<Annotation> getFieldsAnnotations(Object bean) {
        List<Annotation> annotations = new ArrayList<>();
        var fields = bean.getClass().getFields();
        for (var field : fields) {
            annotations.addAll(Arrays.stream(field.getAnnotations()).toList());
        }
        return annotations;
    }

    private List<Annotation> geMethodsAnnotations(Object bean) {
        List<Annotation> annotations = new ArrayList<>();
        var methods = bean.getClass().getMethods();
        for (var method : methods) {
            annotations.addAll(Arrays.stream(method.getAnnotations()).toList());
        }
        return annotations;
    }

    private List<Annotation> getMethodArgsAnnotation(Object bean) {
        List<Annotation> annotations = new ArrayList<>();
        var methods = bean.getClass().getMethods();
        for (var method : methods) {
            var parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                annotations.addAll(List.of(parameter.getAnnotations()));
            }
        }
        return annotations;
    }
}
