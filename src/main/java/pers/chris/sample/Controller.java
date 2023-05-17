package pers.chris.sample;

import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Resource;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/17
 */
@Bean
public class Controller {

    @Resource
    private Service service;

    public void doSomething() {
        service.doSomething();
    }
}
