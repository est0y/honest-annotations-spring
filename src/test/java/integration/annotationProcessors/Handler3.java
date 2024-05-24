package integration.annotationProcessors;

import lombok.Getter;
import org.est0y.honestAnnotations.handlers.AnnotationHandler;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Handler3 implements AnnotationHandler {
    private final List<String> handleBeanClassName = new ArrayList<>();

    @Override
    public Object handle(Object bean, String beanName) {
        handleBeanClassName.add(bean.getClass().getName());
        return bean;
    }
}
