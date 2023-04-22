package ai.openfabric.api.exception;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException(String message) {
        super(message);
    }

    public WorkerNotFoundException() {
        super();
    }
}
