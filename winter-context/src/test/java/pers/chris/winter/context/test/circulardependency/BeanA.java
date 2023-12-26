package pers.chris.winter.context.test.circulardependency;

import pers.chris.winter.context.core.annotation.Component;
import pers.chris.winter.context.core.annotation.Resource;
import pers.chris.winter.context.test.genericinject.base.BaseEntity;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Component
public class BeanA extends BaseEntity {

    @Resource
    private BeanB beanB;

    public void test() {
        this.beanB.print();
    }
}
