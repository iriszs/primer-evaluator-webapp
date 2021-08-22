package nl.bioinf.imgorter.primer_evaluator.model;

import java.util.Optional;

public class PrimerResult<T> {
    private final Optional<Boolean> isPass;
    private final ResultType type;
    private final Optional<String> reason;
    private final Optional<T> value;

    public PrimerResult(boolean isPass, ResultType type) {
        this.isPass = Optional.of(isPass);
        this.type = type;
        this.reason = Optional.empty();
        this.value = Optional.empty();
    }

    public PrimerResult(boolean isPass, ResultType type, String reason, T value) {
        this.isPass = Optional.of(isPass);
        this.type = type;
        this.reason = Optional.of(reason);
        this.value = Optional.of(value);
    }

    public PrimerResult(boolean isPass, ResultType type, T value) {
        this.isPass = Optional.of(isPass);
        this.type = type;
        this.reason = Optional.empty();
        this.value = Optional.of(value);
    }

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
