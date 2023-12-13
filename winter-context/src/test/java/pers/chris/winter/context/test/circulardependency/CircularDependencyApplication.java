package pers.chris.winter.context.test.circulardependency;

import pers.chris.winter.context.core.Applicable;
import pers.chris.winter.context.core.ApplicationExecutor;
import pers.chris.winter.context.core.annotation.Configuration;
import pers.chris.winter.context.core.annotation.Resource;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Configuration
public class CircularDependencyApplication implements Applicable {

    @Resource
    private BeanA beanA;
    @Resource
    private BeanB beanB;

    public static void main(String[] args) {
        new ApplicationExecutor().application(CircularDependencyApplication.class).run();
    }

    @Override
    public void run() {
        beanA.test();
        beanB.test();
    }
}
