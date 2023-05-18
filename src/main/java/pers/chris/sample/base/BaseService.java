package pers.chris.sample.base;

import pers.chris.core.annotation.Resource;
import pers.chris.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/18
 */
public abstract class BaseService<Entity extends BaseEntity, Dao extends BaseDao<Entity>> {

    @Resource
    private Dao dao;

    public void print() {
        dao.print();
        PrintUtil.printClassName(this);
    }
}
