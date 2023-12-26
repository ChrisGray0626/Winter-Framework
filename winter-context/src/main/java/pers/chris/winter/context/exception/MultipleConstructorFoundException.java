package pers.chris.winter.context.exception;

/**
 * @Description
 * @Author Chris
 * @Date 2023/10/25
 */
public class MultipleConstructorFoundException extends RuntimeException {

    public MultipleConstructorFoundException(String beanName) {
        super("Multiple bean constructors found for " + beanName);
    }
}
