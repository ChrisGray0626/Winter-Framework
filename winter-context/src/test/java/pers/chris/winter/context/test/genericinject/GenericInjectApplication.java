package pers.chris.winter.context.test.genericinject;

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
public class GenericInjectApplication implements Applicable {

    @Resource
    private UserController userController;

    public static void main(String[] args) {
        new ApplicationExecutor().application(GenericInjectApplication.class).run();
    }

    public void run() {
        userController.print();
    }
}
