package pers.chris.winter.context.test.genericinject;

import pers.chris.winter.context.core.annotation.Component;
import pers.chris.winter.context.test.genericinject.base.BaseService;


/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Component
public class UserService extends BaseService<User, UserDao> {
}
