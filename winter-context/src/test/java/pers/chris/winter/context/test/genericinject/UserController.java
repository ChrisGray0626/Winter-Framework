package pers.chris.winter.context.test.genericinject;

import pers.chris.winter.context.core.annotation.Component;
import pers.chris.winter.context.test.genericinject.base.BaseController;


/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Component
public class UserController extends BaseController<User, UserDao, UserService> {
}
