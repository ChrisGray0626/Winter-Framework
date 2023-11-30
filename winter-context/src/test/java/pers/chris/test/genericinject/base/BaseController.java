package pers.chris.test.genericinject.base;

import pers.chris.core.annotation.Resource;
import pers.chris.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/18
 */
public abstract class BaseController<Entity extends BaseEntity, Dao extends BaseDao<Entity>, Service extends BaseService<Entity, Dao>> {

    @Resource
    private Service service;

    public void print() {
        service.print();
        PrintUtil.printClassName(this);
    }
}
