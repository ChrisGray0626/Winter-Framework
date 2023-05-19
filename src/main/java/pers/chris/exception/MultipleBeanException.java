package pers.chris.exception;

import pers.chris.util.StringUtil;

import java.util.Collection;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/19
 */
public class MultipleBeanException extends RuntimeException {

    public MultipleBeanException(String fieldName, Collection<String> beanNames) {
        super("Multiple beans found for " + fieldName + ": " + StringUtil.CollectionToString(beanNames));
    }
}
