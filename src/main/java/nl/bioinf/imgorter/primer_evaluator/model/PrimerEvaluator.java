package nl.bioinf.imgorter.primer_evaluator.model;

import java.util.*;

public class PrimerEvaluator {

    private final Calculator CAL = new Calculator();

    public PrimerEvaluator(){

    }

    /**
     * Evaluates one primer of all the characteristics like:
     * - Length
     * - Containing only ACTG
     * - Has 3' and 5' or the other way around
     * - GC percentage
     * - Melting temperature
     * - Maximum homopolymer length
     * - Intramolecular identity
     *
     * @return a list with all results containing: true/false of a characteristic,
     * a reason why it's false and optionally a value.
     */

    public PrimerResult[] evaluateOne(Primer primerA) {
        List<PrimerResult> results = new ArrayList<>();

        // if one of these is false, don't calculate - the sequence will not be valid.
        if(!primerA.getIsValidLength() || !primerA.getIsValidSequence() || !primerA.getIsValidNucs()){
            results.add(new PrimerResult(false, ResultType.SEQUENCE_CONTENT, "The entered sequence is not valid and therefore nothing is calculated. Please try again."));
            return results.toArray(new PrimerResult[0]);
        }
        else{
            // Count individual nucleotides and put in a hashmap
            primerA.setNucleotideCount(CAL.CountNucleotides(primerA.getNucleotides()));
            // Calculate GC percentage of primer using the individual nucleotide counts
            // and set result in primer object
            primerA.setGcPercentage(CAL.calculateGC(primerA.getNucleotideCount()));
            // If the calculated GC percentage is below 50 and above 55 - the primer does not pass
            if (primerA.getGcPercentage() < 50 && (primerA.getGcPercentage() > 55)){
                // fail response
                // Returns fail, reason and the value of the of the GC percentage
                results.add(new PrimerResult(false, ResultType.GC_PER, "GC Percentage is below 50 or above 55", primerA.getGcPercentage()));
            }
            else{
                // Success response
                // Returns true and the value of the GC percentage
                results.add(new PrimerResult(true, ResultType.GC_PER, primerA.getGcPercentage()));
            }

            // Calculate melting temperature of the primer using the individual nucleotide counts
            // and set result in primer object
            primerA.setMeltingTemp(CAL.calculateMeltingTemp(primerA.getNucleotideCount()));
            // The primer does not pass if the Melting Temperature is below 55 and above 65
            if (primerA.getMeltingTemp() < 55 && (primerA.getMeltingTemp() > 65)){
                // Fail scenario
                // Result is set to false, gives the reason and the value of the melting temperature
                results.add(new PrimerResult(false, ResultType.MELTING_TEMP, "Melting temperature is below 55 or above 65", primerA.getMeltingTemp()));
            }
            else{
                // Success response
                // Returns true and the value of the melting temperature
                results.add(new PrimerResult(true, ResultType.MELTING_TEMP, primerA.getMeltingTemp()));
            }

            // Calculate the maximum homopolymer length in a hashmap using the individual nucleotide counts
            // and set result in primer object
            primerA.setHomopolymerLength(CAL.calculateMaxHomopolymerLength(primerA.getNucleotides()));
            // Retrieve the highest maximum homopolymer length out of the list of all nucleotides and their homopolymer
            // lenghts
            Pair<Nucleotide, Integer> highestMaxPolyLen = CAL.getLongestMaxHomopolymerLength(primerA.getNucleotides());
            // Fails if a nucleotide appears over 4 times one after another
            if (highestMaxPolyLen.getB() > 4){
                // Fail scenario
                // Returns false, the reason why it fails including the nucleotide that repeats too many after one another
                // And the length of that homopolymer
                results.add(new PrimerResult(false, ResultType.HOMO_POLY_LEN, String.format("Nucleotide %c has a homopolymer length of 4 or higher", highestMaxPolyLen.getA().getCharacter()), highestMaxPolyLen.getB()));
            }
            else{
                // Success scenario
                // Returns true
                results.add(new PrimerResult(true, ResultType.HOMO_POLY_LEN));
            }

            // Calculate the intramolecular identity of the given primer (the matching to itself)
            primerA.setIntraIdentity(CAL.calculateIntramolecularIdentity(primerA));
            // Intraidentity does not have a pre-determined value when it passes or fails the 'test'
            // So user needs to determine for themselves
            // Adds value and no true or false
            results.add(new PrimerResult(ResultType.INTRA_IDENT, primerA.getIntraIdentity()));

            return results.toArray(new PrimerResult[0]);

        }


    }

    /**
     * In case of two primers, pair the results of both and calculate the intermolecular identity between
     * those two
     *
     * @return a paired list with all results containing: true/false of a characteristic,
     * a reason why it's false and optionally a value.
     */

    public Pair<PrimerResult[], PrimerResult[]> evaluateTwo(Primer primerA, Primer primerB){
        PrimerResult[] a = evaluateOne(primerA);
        // Convert to arraylist so after evaluateOne the list can extend one (to add intermolecular identity)
        // Arrays.asList() isn't enough because the current List is not extendable, so instead a new list is created
        List<PrimerResult> b = new ArrayList<>(Arrays.asList(evaluateOne(primerB)));
        // Calculate and add itermolecular identity to the primer result list
        // Interidentity does not have a pre-determined value when it passes or fails the 'test'
        // So user needs to determine for themselves
        // Adds value and no true or valse
        b.add(new PrimerResult(ResultType.INTER_IDENT, CAL.calculateIntermolecularIdentity(primerA, primerB)));

        return new Pair<>(a, b.toArray(new PrimerResult[0]));
    }
}
