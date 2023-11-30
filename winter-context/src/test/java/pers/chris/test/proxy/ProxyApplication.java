package pers.chris.test.proxy;

import pers.chris.core.Applicable;
import pers.chris.core.ApplicationExecutor;
import pers.chris.core.annotation.Configuration;
import pers.chris.core.annotation.Resource;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Configuration
public class ProxyApplication implements Applicable {

    @Resource
    private BeanA beanA;

    public static void main(String[] args) {
        new ApplicationExecutor().application(ProxyApplication.class).run();
    }

    @Override
    public void run() {
        beanA.print();
    }
}
