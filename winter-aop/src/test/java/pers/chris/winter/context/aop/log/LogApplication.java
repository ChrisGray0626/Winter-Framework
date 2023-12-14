package pers.chris.winter.context.aop.log;

import pers.chris.winter.context.core.Applicable;
import pers.chris.winter.context.core.ApplicationExecutor;
import pers.chris.winter.context.core.annotation.Configuration;
import pers.chris.winter.context.core.annotation.Resource;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
@Configuration(basePackages = "pers.chris.winter.context.aop")
public class LogApplication implements Applicable {

    @Resource
    private LoggedBean loggedBean;

    public static void main(String[] args) {
        new ApplicationExecutor().application(LogApplication.class).run();
    }

    @Override
    public void run() {
        loggedBean.func1();
        loggedBean.func2();
    }
}
