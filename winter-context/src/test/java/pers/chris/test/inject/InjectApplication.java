package pers.chris.test.inject;

import pers.chris.core.Applicable;
import pers.chris.core.ApplicationExecutor;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Configuration;
import pers.chris.core.annotation.Resource;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Configuration
public class InjectApplication implements Applicable {

    @Resource
    private BeanWithoutAnnotation beanWithoutAnnotation;
    @Resource
    private BeanWithAnnotation beanWithAnnotation;
    @Resource
    private IBean bean;

    public static void main(String[] args) {
        new ApplicationExecutor().application(InjectApplication.class).run();
    }

    @Bean
    public BeanWithoutAnnotation user() {
        return new BeanWithoutAnnotation();
    }

    @Override
    public void run() {
        beanWithoutAnnotation.print();
        beanWithAnnotation.print();
        bean.print();
    }
}
