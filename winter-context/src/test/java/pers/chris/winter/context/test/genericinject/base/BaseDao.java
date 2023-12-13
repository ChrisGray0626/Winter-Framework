package pers.chris.winter.context.test.genericinject.base;

import pers.chris.winter.context.core.annotation.Resource;
import pers.chris.winter.context.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/18
 */
public abstract class BaseDao<Entity extends BaseEntity> {

    @Resource
    private Entity entity;

    public void print() {
        entity.print();
        PrintUtil.printClassName(this);
    }
}
