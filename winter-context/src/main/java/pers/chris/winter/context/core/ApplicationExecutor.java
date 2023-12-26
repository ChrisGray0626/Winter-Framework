package pers.chris.winter.context.core;

/**
 * @Description Application Executor
 * @Author Chris
 * @Date 2023/5/17
 */
public class ApplicationExecutor {

    private Class<? extends Applicable>[] applicationClasses;

    @SafeVarargs
    public final ApplicationExecutor application(Class<? extends Applicable>... applicationClasses) {
        this.applicationClasses = applicationClasses;
        return this;
    }

    public void run() {
        for (Class<? extends Applicable> application : applicationClasses) {
            ApplicationContext applicationContext = new ApplicationContext(application);
            Applicable applicationBean = (Applicable) applicationContext.getBean(application.getSimpleName());
            applicationBean.run();
        }
    }
}
