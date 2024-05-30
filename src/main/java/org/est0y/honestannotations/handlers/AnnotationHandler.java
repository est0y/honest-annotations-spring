package org.est0y.honestannotations.handlers;

public interface AnnotationHandler {

    Object handle(Object bean, String beanName);
}
