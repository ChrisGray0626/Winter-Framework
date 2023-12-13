package pers.chris.winter.context.aop.exception;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
public class AopConfigurationException extends RuntimeException {

    public AopConfigurationException(String message) {
        super(message);
    }

    public AopConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
