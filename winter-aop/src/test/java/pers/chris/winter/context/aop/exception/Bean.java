package pers.chris.winter.context.aop.exception;

import pers.chris.winter.context.core.annotation.Component;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
@Component
@ExceptionPointcut
public class Bean {

    public void throwHandledException() {
        throw new HandledException();
    }

    public void throwUnhandledException() {
        throw new UnhandledException();
    }
}
