package nl.bioinf.imgorter.primer_evaluator.model;

import nl.bioinf.imgorter.primer_evaluator.model.annotations.Nullable;

public enum Nucleotide {
    A('A', 'T'),
    C('C', 'G'),
    G('G', 'C'),
    T('T', 'A');

    private char nuc;
    private char pairsWith;
    private Nucleotide(char nuc, char pairsWith){
        this.nuc = nuc;
        this.pairsWith = pairsWith;
    }

    /**
     * Get the value of the current nucleotide as a character
     * @return Returns the value of the nucleotide as a capital ASCII character
     */
    public char getCharacter() {
        return this.nuc;
    }

    /**
     * Check if a nucleotide pairs with the current nucleotide to form a base pair
     * @param nuc The nucleotide
     * @return Returns true if it pairs, false if it does not
     */
    public boolean pairsWith(Nucleotide nuc) {
        char nucChar = nuc.getCharacter();
        return nucChar == this.pairsWith;
    }

    /**
     * Determines the complement string of a primer
     * Example: ATCG will become TAGC
     * @return Complement String of primer
     */
    public Nucleotide getComplement() {
        return Nucleotide.fromChar(this.pairsWith);

    }

    /**
     * Get an instance of nucleotide from a character.
     * @param c The character to derive a nucleotide from
     * @return Returns the nucleotide associated with the provided char, or null if the provided char matches no nucleotide
     */
    @Nullable
    public static Nucleotide fromChar(char c) {
        switch(c) {
            case 'A':
            case 'a':
                return Nucleotide.A;
            case 'C':
            case 'c':
                return Nucleotide.C;
            case 'G':
            case 'g':
                return Nucleotide.G;
            case 'T':
            case 't':
                return Nucleotide.T;
        }

        return null;
    }

    public boolean equals(char c) {
        return this.nuc == c;
    }

    public boolean equals(Nucleotide n) {
        return this.nuc == n.getCharacter();
    }


}
