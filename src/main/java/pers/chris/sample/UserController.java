package pers.chris.sample;

import pers.chris.core.annotation.Bean;
import pers.chris.sample.base.BaseController;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/17
 */
@Bean
public class UserController extends BaseController<User, UserDao, UserService> {
}
