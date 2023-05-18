package pers.chris.sample;

import pers.chris.core.ApplicationExecutor;
import pers.chris.core.BaseApplication;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.IoCApplication;
import pers.chris.core.annotation.Resource;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/17
 */
@IoCApplication
@Bean
public class Application extends BaseApplication {

    @Resource
    private UserController userController;

    public static void main(String[] args) {
        new ApplicationExecutor().source(Application.class).run();
    }

    @Override
    public void run() {
        userController.print();
    }
}
