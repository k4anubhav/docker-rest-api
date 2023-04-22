package ai.openfabric.api.exception;

public class NotFoundErrorResponse extends ErrorResponse {
    public NotFoundErrorResponse() {
        this("Not found");
    }

    public NotFoundErrorResponse(String message) {
        super(message, "not_found");
    }
}
