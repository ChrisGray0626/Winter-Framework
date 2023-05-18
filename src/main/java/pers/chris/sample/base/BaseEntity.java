package pers.chris.sample.base;

import pers.chris.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/18
 */
public class BaseEntity {
    public void print() {
        PrintUtil.printClassName(this);
    }
}
