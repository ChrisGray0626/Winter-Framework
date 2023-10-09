package pers.chris.sample;

import pers.chris.core.annotation.Component;
import pers.chris.sample.base.StudentService;
import pers.chris.util.PrintUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/19
 */
@Component
public class StudentStudentServiceImpl implements StudentService {

    @Override
    public void print() {
        PrintUtil.printClassName(this);
    }
}
