package pers.chris.exception;

/**
 * @Description Missing Application Bean Exception
 * @Author Chris
 * @Date 2023/5/19
 */
public class ApplicationMissingException extends RuntimeException {

    public ApplicationMissingException() {
        super("Missing Application bean");
    }
}
