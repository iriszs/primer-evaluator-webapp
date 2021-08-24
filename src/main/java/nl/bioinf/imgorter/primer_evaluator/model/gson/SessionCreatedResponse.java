package nl.bioinf.imgorter.primer_evaluator.model.gson;

@SuppressWarnings("unused")
public class SessionCreatedResponse {

    private final String sessionId;
    private final long expiry;

    public SessionCreatedResponse(String sessionId, long expiry) {
        this.sessionId = sessionId;
        this.expiry = expiry;
    }
}
