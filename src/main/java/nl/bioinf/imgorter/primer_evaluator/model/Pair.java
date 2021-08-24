package nl.bioinf.imgorter.primer_evaluator.model;

/**
 * Used to be able to use PrimerResult.A and PrimerResult.B
 */


public class Pair<A, B> {
    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return this.a;
    }

    public B getB() {
        return this.b;
    }
}
