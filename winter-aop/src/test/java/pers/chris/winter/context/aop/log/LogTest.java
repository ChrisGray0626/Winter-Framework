package pers.chris.winter.context.aop.log;

import org.junit.jupiter.api.Test;
import pers.chris.winter.context.core.ApplicationContext;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
public class LogTest {

    @Test
    public void test() {
        ApplicationContext applicationContext = new ApplicationContext(LogApplication.class);
        LoggedBean loggedBean = (LoggedBean) applicationContext.getBean("LoggedBean");

        loggedBean.func1();
        loggedBean.func2();
    }
}
