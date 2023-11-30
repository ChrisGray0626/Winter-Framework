package pers.chris.test.genericinject;

import pers.chris.core.annotation.Component;
import pers.chris.test.genericinject.base.BaseService;


/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Component
public class UserService extends BaseService<User, UserDao> {
}