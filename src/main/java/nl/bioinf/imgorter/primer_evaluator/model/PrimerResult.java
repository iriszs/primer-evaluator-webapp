package nl.bioinf.imgorter.primer_evaluator.model;

import java.util.Optional;

public class PrimerResult<T> {
    private final Optional<Boolean> isPass;
    private final ResultType type;
    private final Optional<String> reason;
    private final Optional<T> value;

    /**
     * PrimerResult to return True and the type (like GC_PERC)
     * @param isPass true or false
     * @param type resulttype from enum ResultType
     */
    public PrimerResult(boolean isPass, ResultType type) {
        this.isPass = Optional.of(isPass);
        this.type = type;
        this.reason = Optional.empty();
        this.value = Optional.empty();
    }

    /**
     * PrimerResult to return False, the type (like GC_PERC), the reason why it did not pass and the corresponding
     * value
     * @param isPass true or false
     * @param type resulttype from enum ResultType
     * @param reason String that explains why it did not pass
     * @param value the corresponding value of the not passed type
     */
    public PrimerResult(boolean isPass, ResultType type, String reason, T value) {
        this.isPass = Optional.of(isPass);
        this.type = type;
        this.reason = Optional.of(reason);
        this.value = Optional.of(value);
    }

    /**
     * PrimerResult to return True or False, the type (like GC_PERC), and the value.
     * @param isPass true or false
     * @param type resulttype from enum ResultType
     * @param value the corresponding value of the not passed type
     */
    public PrimerResult(boolean isPass, ResultType type, T value) {
        this.isPass = Optional.of(isPass);
        this.type = type;
        this.reason = Optional.empty();
        this.value = Optional.of(value);
    }

    /**
     * PrimerResult to return only the type and the value of the calculated characteristic.
     * This is used in cases that there is no straight forward pass (true) or not pass(false)
     * @param type resulttype from enum ResultType
     * @param value the corresponding value of the not passed type
     */
    public PrimerResult(ResultType type, T value) {
        this.isPass = Optional.empty();
        this.type = type;
        this.reason = Optional.empty();
        this.value = Optional.of(value);
    }

    public Optional<Boolean> getIsPass() {
        return this.isPass;
    }

    public ResultType getType() {
        return  this.type;
    }

    public Optional<String> getReason() {
        return this.reason;
    }

    public Optional<T> getValue() {
        return this.value;
    }
}
