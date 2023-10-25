package pers.chris.exception;

/**
 * @Description
 * @Author Chris
 * @Date 2023/10/25
 */
public class NoConstructorFoundException extends RuntimeException {

    public NoConstructorFoundException(String beanName) {
        super("No constructor found for " + beanName);
    }
}
