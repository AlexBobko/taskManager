package loc.task.service.exc;

public class TaskServiceException extends Exception {

    private Exception exception;

    public TaskServiceException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
