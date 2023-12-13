package pers.chris.winter.context.exception;

/**
 * @Description
 * @Author Chris
 * @Date 2023/10/24
 */
public class DuplicateBeanDefinition extends RuntimeException {

    public DuplicateBeanDefinition(String beanName) {
        super("Duplicate bean definition for " + beanName);
    }
}
