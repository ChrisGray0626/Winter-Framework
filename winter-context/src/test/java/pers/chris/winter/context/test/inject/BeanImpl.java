package pers.chris.winter.context.test.inject;

import pers.chris.winter.context.core.annotation.Component;
import pers.chris.winter.context.util.PrintUtil;

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
