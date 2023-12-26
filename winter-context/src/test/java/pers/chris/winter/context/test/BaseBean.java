package pers.chris.winter.context.test;

import pers.chris.winter.context.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
public abstract class BaseBean {

    public void print() {
        PrintUtil.printClassName(this);
    }

}
