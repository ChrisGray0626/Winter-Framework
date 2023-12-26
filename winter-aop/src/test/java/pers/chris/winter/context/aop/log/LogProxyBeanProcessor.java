package pers.chris.winter.context.aop.log;

import pers.chris.winter.context.aop.core.BaseAnnotationProxyBeanPostProcessor;
import pers.chris.winter.context.core.annotation.Component;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
@Component
public class LogProxyBeanProcessor extends BaseAnnotationProxyBeanPostProcessor<LogPointcut> {
}
