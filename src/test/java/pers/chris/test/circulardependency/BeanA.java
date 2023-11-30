package pers.chris.test.circulardependency;

import pers.chris.core.annotation.Component;
import pers.chris.core.annotation.Resource;
import pers.chris.sample.base.BaseEntity;

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
