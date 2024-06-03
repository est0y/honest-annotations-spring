# Honest Annotations

Honest Annotations is a library for creating declarative annotations in Spring, which simplifies annotation handling and provides more explicit control over how annotations are processed.

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.est0y</groupId>
    <artifactId>honest-annotations-spring</artifactId>
    <version>0.0.1</version>
</dependency>
```
### Gradle
```
implementation 'com.est0y:honest-annotations-spring:0.0.1'
```

## Quick Start

### Using with Spring (without Spring Boot)

If you are using Spring without Spring Boot, you need to add the `@EnableHonestAnnotations` annotation. With Spring Boot, this annotation is not required.

### Example Usage

1. Create your custom annotation and specify the handler using `@AfterInitialization`:

    ```java
    import java.lang.annotation.ElementType;
    import java.lang.annotation.Retention;
    import java.lang.annotation.RetentionPolicy;
    import java.lang.annotation.Target;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @AfterInitialization(UserAnnotationHandler.class)
    public @interface UserAnnotation {
    }
    ```

2. Annotate your class with the custom annotation:

    ```java
    import org.springframework.stereotype.Component;

    @UserAnnotation
    @Component
    public class UserClass {
        public void method() {
            System.out.println("origin method");
        }
    }
    ```

3. Create a handler class for your annotation by implementing the `AnnotationHandler` interface:

    ```java
    import org.springframework.aop.framework.ProxyFactory;
    import org.aopalliance.intercept.MethodInvocation;
    import org.springframework.stereotype.Component;
    import org.springframework.lang.NonNull;

    @Component
    public class UserAnnotationHandler implements AnnotationHandler {
        @Override
        public Object handle(Object bean, String beanName) {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.addAdvice(new org.aopalliance.intercept.MethodInterceptor() {
                @Override
                public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
                    System.out.printf("%s -> %s%n", invocation.getThis(), invocation.getMethod().getName());
                    return invocation.getMethod().invoke(invocation.getThis(), invocation.getArguments());
                }
            });
            return proxyFactory.getProxy();
        }
    }
    ```
    
## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
