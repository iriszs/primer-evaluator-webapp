package nl.bioinf.imgorter.primer_evaluator.model.gson;

import nl.bioinf.imgorter.primer_evaluator.model.PrimerResult;

public class FileUploadResponse {
    private final PrimerResult[] a;
    private final PrimerResult[] b;

    public FileUploadResponse(PrimerResult[] a, PrimerResult[] b) {
        this.a = a;
        this.b = b;
    }
}
