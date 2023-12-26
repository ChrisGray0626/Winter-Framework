package pers.chris.winter.context.test.inject;

import pers.chris.winter.context.core.Applicable;
import pers.chris.winter.context.core.ApplicationExecutor;
import pers.chris.winter.context.core.annotation.Bean;
import pers.chris.winter.context.core.annotation.Configuration;
import pers.chris.winter.context.core.annotation.Resource;

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
