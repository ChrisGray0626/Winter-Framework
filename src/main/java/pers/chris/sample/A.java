package pers.chris.sample;

import pers.chris.core.annotation.Resource;
import pers.chris.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/20
 */
//@Component
public class A {

    @Resource
    private B b;

    public void test() {
        b.print();
    }

    public void print() {
        PrintUtil.printClassName(this);
    }
}
