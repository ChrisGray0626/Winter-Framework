package pers.chris.sample;

import pers.chris.core.ApplicationExecutor;
import pers.chris.core.BaseApplication;
import pers.chris.core.annotation.Application;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Resource;
import pers.chris.sample.base.StudentService;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/17
 */
@Application(basePackages = {"pers.chris.sample"})
@Bean
public class SampleApplication extends BaseApplication {

    @Resource
    private UserController userController;
    @Resource
    private StudentService studentService;

    public static void main(String[] args) {
        new ApplicationExecutor().application(SampleApplication.class).run();
    }

    @Override
    public void run() {
        userController.print();
        studentService.print();
    }
}
