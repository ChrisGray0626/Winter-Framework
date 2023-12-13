package pers.chris.winter.context.aop;

import pers.chris.winter.context.core.Applicable;
import pers.chris.winter.context.core.ApplicationExecutor;
import pers.chris.winter.context.core.annotation.Configuration;
import pers.chris.winter.context.core.annotation.Resource;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
@Configuration
public class AnnoApplication implements Applicable {

    @Resource
    private A a;

    public static void main(String[] args) {
        new ApplicationExecutor().application(AnnoApplication.class).run();
    }

    @Override
    public void run() {
        a.test();
        a.test2();
    }
}
