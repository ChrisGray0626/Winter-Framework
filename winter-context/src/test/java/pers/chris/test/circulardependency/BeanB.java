package pers.chris.test.circulardependency;

import pers.chris.core.annotation.Component;
import pers.chris.core.annotation.Resource;
import pers.chris.test.genericinject.base.BaseEntity;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Component
public class BeanB extends BaseEntity {

    @Resource
    private BeanA beanA;

    public void test() {
        this.beanA.print();
    }
}
