package pers.chris.test.genericinject;

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
