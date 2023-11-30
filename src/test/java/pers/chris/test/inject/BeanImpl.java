package pers.chris.test.inject;

import pers.chris.core.annotation.Component;
import pers.chris.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Component
public class BeanImpl implements IBean {
    @Override
    public void print() {
        PrintUtil.printClassName(this);
    }
}
