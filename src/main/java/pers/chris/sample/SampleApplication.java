package pers.chris.sample;

import pers.chris.core.Applicable;
import pers.chris.core.ApplicationExecutor;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Component;
import pers.chris.core.annotation.Configuration;
import pers.chris.core.annotation.Resource;
import pers.chris.sample.base.StudentService;

/**
 * @Description Sample Application
 * @Author Chris
 * @Date 2023/5/17
 */
@Configuration(basePackages = {"pers.chris.sample"})
@Component
public class SampleApplication extends Applicable {

    @Resource
    private UserController userController;
    @Resource
    private StudentService studentService;
    @Resource
    private A a;
    @Resource
    private B b;

    @Bean
    public A a() {
        return new A();
    }

    @Bean
    public B b() {
        return new B();
    }

    public static void main(String[] args) {
        new ApplicationExecutor().application(SampleApplication.class).run();
    }

    @Override
    public void run() {
        // Sample circular dependency
        a.test();
        b.test();
        // Sample generic dependency
        userController.print();
        studentService.print();
    }
}
