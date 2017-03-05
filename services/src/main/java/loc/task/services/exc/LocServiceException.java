package loc.task.services.exc;

public class LocServiceException extends Exception {

    private Exception exception;

    public LocServiceException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
