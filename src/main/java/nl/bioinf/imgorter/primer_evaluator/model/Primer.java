package nl.bioinf.imgorter.primer_evaluator.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This primer class contains all getters and setters of each characteristic that a primer can have
 */

public class Primer {
    private final String sequence;
    private Nucleotide[] nucleotides;
    private boolean isForward;
    private String baseSequence;
    private boolean validLength;
    private boolean validNucs;
    private boolean validSequence;
    private HashMap<Nucleotide, Integer> nucCount;
    private double gcPercentage;
    private int meltingTemp;
    private HashMap <Nucleotide, Integer> maxHomopolymerLengths;
    private int interIdentity;
    private int intraIdentity;
    private HashMap<String, String> evaluatedResults;

    /**
     * Create a new Primer object
     * @param sequence The sequence String of the primer
     */
    public Primer(String sequence){

        this.sequence = sequence;
        this.isForward = sequence.startsWith("5'-");
        // if the sequence startswith 5'- it is a forward primer.
        if(isForward) {
            this.baseSequence = sequence.substring(sequence.indexOf('5') + 3, sequence.indexOf('3') -1);
        } else {
            this.baseSequence = sequence.substring(sequence.indexOf('3') + 3, sequence.indexOf('5') -1);
        }

        char[] sequenceChars = this.baseSequence.toCharArray();
        Nucleotide[] nucleotides = new Nucleotide[sequenceChars.length];

        for(int i = 0; i < sequenceChars.length; i++) {
            nucleotides[i] = Nucleotide.fromChar(sequenceChars[i]);
        }
        this.nucleotides = nucleotides;

    }

    public String getSequence() {
        return this.sequence;
    }

    public Nucleotide[] getNucleotides(){
        return this.nucleotides;
    }

    /**
     * Gets the reverse of a primer
     * Primer 5'-ACTG-3' will become 3-'GTCA-5'
     * @return new Primer object but in reversed sequence
     *
     */

    public Primer getReverse() {
        final String reversedNucleotideSeq = new StringBuilder(this.baseSequence).reverse().toString();

        String reversedSeq;
        if (this.isForward) {
            reversedSeq = String.format("3'-%s-5'", reversedNucleotideSeq);
        } else {
            reversedSeq = String.format("5'-%s-3'", reversedNucleotideSeq);
        }

        return new Primer(reversedSeq);
    }

    /**
     * Gets the COMPLEMENT of a primer
     * Primer 5'-ACTG-3' will become 5-'TGAC-3'
     * A binds to a T C binds to a G (they are complements)
     * @return new Primer object but in reversed sequence
     *
     */
    public Primer getComplement() {
        final char[] complementSeqChars = new char[this.nucleotides.length];
        for(int i = 0; i < this.nucleotides.length; i++) {
            Nucleotide base = this.nucleotides[i];
            complementSeqChars[i] = this.nucleotides[i].getComplement().getCharacter();
        }

        String complementSeq;
        if(this.isForward) {
            complementSeq = String.format("3'-%s-5'", String.valueOf(complementSeqChars));
        } else {
            complementSeq = String.format("5'-%s-3'", String.valueOf(complementSeqChars));
        }

        return new Primer(complementSeq);
    }

    public String getBaseSequence(){
        return this.baseSequence;
    }

    public void setIsValidLength(boolean validLength){
        this.validLength = validLength;
    }

    public Boolean getIsValidLength(){
        return validLength;
    }

    public void setIsValidNucs(boolean validNucs){
        this.validNucs = validNucs;
    }

    public Boolean getIsValidNucs(){
        return validNucs;
    }

    public void setIsValidSequence(boolean validSequence){
        this.validSequence = validSequence;
    }

    public Boolean getIsValidSequence(){
        return validSequence;
    }

    public void setNucleotideCount(HashMap<Nucleotide, Integer> baseCount){
        this.nucCount = baseCount;
    }

    public HashMap<Nucleotide, Integer> getNucleotideCount() {
        return nucCount;
    }

    public void setGcPercentage(double gcPercentage) {
        this.gcPercentage = gcPercentage;
    }

    public double getGcPercentage(){
        return gcPercentage;
    }
    public void setMeltingTemp(int meltingTemp){
        this.meltingTemp = meltingTemp;
    }

    public int getMeltingTemp(){

        return meltingTemp;
    }

    public void setHomopolymerLength(HashMap <Nucleotide, Integer> maxHomopolymerLength){
        this.maxHomopolymerLengths = maxHomopolymerLength;

    }

    public HashMap<Nucleotide, Integer> getHomopolymerMap() {

        return maxHomopolymerLengths;
    }



    public HashMap<Nucleotide, Integer> getMaxHomopolymerLength() {
        HashMap<Nucleotide, Integer> maxHomopolymer = new HashMap<Nucleotide, Integer>();
        int maxValueInMap=(Collections.max(maxHomopolymerLengths.values()));
        for (Map.Entry<Nucleotide, Integer> entry : maxHomopolymerLengths.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                maxHomopolymer.put(entry.getKey(), entry.getValue());
            }
        }

        return maxHomopolymer;
    }

    public void setInterIdentity(int interIdentity){
        this.interIdentity = interIdentity;
    }

    public int getInterIdentity(){
        return interIdentity;
    }

    public void setIntraIdentity(int intraIdentity){
        this.intraIdentity = intraIdentity;
    }

    public int getIntraIdentity(){
        return intraIdentity;
    }

    public void setEvaluatedResults(HashMap <String, String> evaluatedResults){
        this.evaluatedResults = evaluatedResults;
    }

    public HashMap<String, String> getEvaluatedResults(){
        return evaluatedResults;
    }

}
