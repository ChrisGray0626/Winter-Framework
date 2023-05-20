package pers.chris.sample;

import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Resource;
import pers.chris.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/20
 */
@Bean
public class B {

    @Resource
    private A a;

    public void test() {
        a.print();
    }

    public void print() {
        PrintUtil.printClassName(this);
    }
}
