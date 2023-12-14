package pers.chris.winter.context.test.circulardependency;

import org.junit.jupiter.api.Test;
import pers.chris.winter.context.core.Applicable;
import pers.chris.winter.context.core.ApplicationContext;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
public class ApplicationTest {

    @Test
    public void run() {
        ApplicationContext applicationContext = new ApplicationContext(CircularDependencyApplication.class);
        Applicable applicationBean = (Applicable) applicationContext.getBean(CircularDependencyApplication.class.getSimpleName());
        applicationBean.run();
    }
}
