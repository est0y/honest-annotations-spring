package integration.annotationProcessors;

import com.est0y.honestannotations.handlers.AnnotationHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Handler1 implements AnnotationHandler {
    private final List<String> handleBeanClassName = new ArrayList<>();

    @Override
    public Object handle(Object bean, String beanName) {
        handleBeanClassName.add(bean.getClass().getName());
        return bean;
    }
}
