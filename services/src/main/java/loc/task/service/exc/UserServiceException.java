package loc.task.service.exc;

public class UserServiceException extends Exception {

    private Exception exception;

    public UserServiceException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
