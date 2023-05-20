package pers.chris.exception;

import pers.chris.util.StringUtil;

import java.util.Collection;

/**
 * @Description Multiple Beans Found Exception
 * @Author Chris
 * @Date 2023/5/19
 */
public class MultipleBeanFoundException extends RuntimeException {

    public MultipleBeanFoundException(String fieldName, Collection<String> beanNames) {
        super("Multiple beans found for " + fieldName + ": " + StringUtil.CollectionToString(beanNames));
    }
}
