package safran.pfe.exceptions;

public class WorkflowSaveException extends RuntimeException {

    public WorkflowSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}