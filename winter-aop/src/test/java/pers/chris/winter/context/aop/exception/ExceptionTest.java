package pers.chris.winter.context.aop.exception;

import org.junit.jupiter.api.Test;
import pers.chris.winter.context.core.ApplicationContext;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
public class ExceptionTest {

    @Test
    public void test() {
        ApplicationContext applicationContext = new ApplicationContext(ExceptionApplication.class);
        Bean bean = (Bean) applicationContext.getBean("Bean");
        bean.throwHandledException();
        bean.throwUnhandledException();
    }
}
