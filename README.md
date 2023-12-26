# Abstract

It is a simple **Inversion of Control (IoC)** framework integrated the ability to **Aspect Oriented Programming (AOP)**,
which is referenced from Spring.

Components of Winter Framework:

- winter-context: Core IoC container that support mutiple ways of injection.
- winter-aop: Core AOP component based on annotation and dynamic proxy.
- winter-util: Tool component that provides tool classes to support other component.

# Function Introduction

## Bean Register

### Annotation Register

`@Componenet` can be used to register the bean, which is only supported to modify the **class**.

For example:

```java
@Component
public class BeanA {
  ...
}
```

### Function Register

`@Bean` can be used to register the bean, which is only used to modify the **function**.

```java
    @Bean
    public BeanB beanB() {
        return new BeanB();
    }
```

*Argument settings of the function are not supported currently.*

## Bean Injection

`@Resource` can be used to inject the bean. The order of search matches is as follows:

1. Bean name
1. Bean type including the **same type** and **super type**, by this way, **interface injection** can be achieved.
1. Bean generic type, by this way, **generic injection** can be achieved.

### Interface Injection

An interface can be defined first and called by a bean. An **implementation bean** of the interface will be injected.

*Mutiple implementation beans are not supported currently.*

For example:

```java
public interface IBean {

    void print();
}

@Component
public class BeanImpl implements IBean {
    @Override
    public void print() {
        PrintUtil.printClassName(this);
    }
}

public class InjectApplication implements Applicable {

    @Resource
    private IBean bean;
  	...
}
```

### Generic Injection

Generic bean can be injected by matching the generic bean and implementation bean.

**<u>This feature is not supported until Spring 4.0.</u>**

For example:

```java
public abstract class BaseEntity {
    public void print() {
        PrintUtil.printClassName(this);
    }
}

public abstract class BaseDao<Entity extends BaseEntity> {

    @Resource
    private Entity entity;

    public void print() {
        entity.print();
        PrintUtil.printClassName(this);
    }
}

@Component
public class User extends BaseEntity {
}

@Component
public class UserDao extends BaseDao<User> {
}
```

By this way, the template can be built by the generic class.

## Dynamic Proxy

When beans are managed by the container, the **proxy pattern** for the specified bean can be implemented.

`BeanPostProcessor` is a special bean, the aim of which is to proxy other beans. The time of proxy processing can be
divided into **before initialization** and **after initialization**.

For example:

```java
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}

@Component
public class ProxyBeanPostAfterInitializationProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("Bean " + beanName + " is proxied after initialization");

        return bean;
    }
}

@Component
public class ProxyBeanPostBeforeInitializationProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof BeanA) {
            return new ProxiedBeanA(bean);
        }
        return bean;
    }
}
```

## AOP

The custom **annotation** can be used to define the **pointcut** of the aspect by modifying the class. The custom *
*handler bean** implemented by `InvocationHandler` can be used to define the **advice** of the aspect.

For example:

```java

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogPointcut {
    String value() default "LogInvocationHandler";
}

@Component
public class LogInvocationHandler implements BeforeInvocationHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogInvocationHandler.class);

    @Override
    public void doBefore(Object proxy, Method method, Object[] args) {
        LOGGER.info("Method {} is invoked.", method.getName());
    }
}

@LogPointcut
@Component
public class LoggedBean {

    public void func1() {
    }

    public void func2() {
    }
}
```

*The annotation defined as the pointcut is not supported to modify the method currently.*

# Reference

- https://www.liaoxuefeng.com/wiki/1539348902182944