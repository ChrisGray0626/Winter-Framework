package pers.chris.winter.context.test.genericinject.base;

import pers.chris.winter.context.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/18
 */
public abstract class BaseEntity {
    public void print() {
        PrintUtil.printClassName(this);
    }
}
