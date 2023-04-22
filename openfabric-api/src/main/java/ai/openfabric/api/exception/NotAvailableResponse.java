package ai.openfabric.api.exception;

public class NotAvailableResponse extends ErrorResponse {
    public NotAvailableResponse() {
        this("Not available");
    }

    public NotAvailableResponse(String message) {
        super(message, "not_available");
    }
}
