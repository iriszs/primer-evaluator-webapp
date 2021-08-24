package nl.bioinf.imgorter.primer_evaluator.model.gson;

@SuppressWarnings("unused")
public class ErrorResponse {

    private String location;
    private String message;

    public ErrorResponse(String location, String message) {
        this.location = location;
        this.message = message;
    }
}
