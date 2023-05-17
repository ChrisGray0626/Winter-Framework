package pers.chris.sample;

import pers.chris.core.annotation.Bean;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/17
 */
@Bean
public class Service {

    public void doSomething() {
        System.out.println("Service.doSomething()");
    }
}
