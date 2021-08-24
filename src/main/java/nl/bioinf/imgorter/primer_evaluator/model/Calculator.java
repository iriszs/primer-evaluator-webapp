package nl.bioinf.imgorter.primer_evaluator.model;

import java.util.*;
import java.util.stream.Collectors;

public class Calculator {

    /**
     * Counts the number of nucleotides in the given primer
     *
     * @param nucleotides Map of individual nucleotides (sequence without 5'- and 3-') to calculate from
     * @return Returns a HashMap where the key is the counted nucleotide and the value the occurrence count
     */
    public HashMap<Nucleotide, Integer> CountNucleotides(Nucleotide[] nucleotides){
        HashMap<Nucleotide, Integer> nucCount = new HashMap ();

        int countA = 0;
        int countC = 0;
        int countT = 0;
        int countG = 0;

        for (Nucleotide nuc : nucleotides) {
            if(nuc.equals(Nucleotide.A)){
                countA++;
            } else if (nuc.equals(Nucleotide.C)){
                countC++;
            } else if (nuc.equals(Nucleotide.T)){
                countT++;
            } else if (nuc.equals(Nucleotide.G)){
                countG++;
            }
        }

        nucCount.put(Nucleotide.A, countA);
        nucCount.put(Nucleotide.C, countC);
        nucCount.put(Nucleotide.T, countT);
        nucCount.put(Nucleotide.G, countG);

        return nucCount;
    }

    /**
     * Calculates the percentage of GC nucleotides in the given
     * Uses the formula: number of G and C nucleotides / number of all nucleotides * 100%
     * @param nucCount the HasMap containing the occurence counts of each nucleotide
     * @return Returns the GCpercentage as a double
     */

    public double calculateGC(HashMap<Nucleotide, Integer> nucCount){

        double GCPercentage;
        int countA = nucCount.get(Nucleotide.A);
        int countC = nucCount.get(Nucleotide.C);
        int countT = nucCount.get(Nucleotide.T);
        int countG = nucCount.get(Nucleotide.G);

        GCPercentage = (double) (countG + countC) / (countA + countC + countG + countT) * 100;

        return GCPercentage;
    }

    /**
     * Calculates the melting temperature of the given primer
     * Uses the formula: (4 * the number of G and C nucleotides) + (2 * the number of A and T nucleotides)
     * @param nucCount the HasMap containing the occurrence counts of each nucleotide
     * @return Returns the melting temperature as an integer (temperature in Celsius)
     */

    public int calculateMeltingTemp(HashMap<Nucleotide, Integer> nucCount){

        int meltingTemp;

        int countA = nucCount.get(Nucleotide.A);
        int countC = nucCount.get(Nucleotide.C);
        int countT = nucCount.get(Nucleotide.T);
        int countG = nucCount.get(Nucleotide.G);

        meltingTemp = (int) (4* (countG + countC)) + (2* (countA + countT));

        return meltingTemp;
    }

    /**
     * Calculates the maximum homopolymer length (longest sequence of continuous nucleotides) for every nucleotide
     * For the input "AAACCCGGGTTTTAAAA", A = 4, C = 3, G = 3, T = 4
     *
     * @param nucleotides The individual nucleotide map (base sequence without 5'- and 3-') to calculate from
     * @return Returns HashMap where the key is the counted nucleotide and the value is the continuous occurrence count
     * Returns a HashMap instead of integer to have more information stored to request later
     */
    public HashMap<Nucleotide, Integer> calculateMaxHomopolymerLength(Nucleotide[] nucleotides) {
        final HashMap<Nucleotide, Integer> homopolymerLenghts = new HashMap<>();

        int aCounter = 0;
        int cCounter = 0;
        int gCounter = 0;
        int tCounter = 0;

        Nucleotide previousNuc = null;

        for(int i = 0; i < nucleotides.length; i++) {
            Nucleotide nuc = nucleotides[i];

            if(previousNuc != null && previousNuc == nuc) {
                switch(nuc) {
                    case A:
                        aCounter++;
                        break;
                    case C:
                        cCounter++;
                        break;
                    case G:
                        gCounter++;
                        break;
                    case T:
                        tCounter++;
                        break;
                }
            } else if(previousNuc != null){
                switch(previousNuc) {
                    case A:
                        insertIfLarger(homopolymerLenghts, previousNuc, ++aCounter);
                        aCounter = 0;
                        break;
                    case C:
                        insertIfLarger(homopolymerLenghts, previousNuc, ++cCounter);
                        cCounter = 0;
                        break;
                    case G:
                        insertIfLarger(homopolymerLenghts, previousNuc, ++gCounter);
                        gCounter = 0;
                        break;
                    case T:
                        insertIfLarger(homopolymerLenghts, previousNuc, ++tCounter);
                        tCounter = 0;
                        break;
                }
            }

            previousNuc = nuc;
        }

        switch(previousNuc) {
            case A:
                insertIfLarger(homopolymerLenghts, previousNuc, ++aCounter);
                break;
            case C:
                insertIfLarger(homopolymerLenghts, previousNuc, ++cCounter);
                break;
            case G:
                insertIfLarger(homopolymerLenghts, previousNuc, ++gCounter);
                break;
            case T:
                insertIfLarger(homopolymerLenghts, previousNuc, ++tCounter);
                break;
        }

        return homopolymerLenghts;
    }

    // Function to fill up the maximum homopolymer length map.
    // To iterate and only add a new count when it's larger than the previous count
    private void insertIfLarger(final HashMap<Nucleotide, Integer> polymerMap, Nucleotide nuc, int nucCount) {
        Integer currentCount = polymerMap.get(nuc);
        if(currentCount == null) {
            polymerMap.put(nuc, nucCount);
            return;
        }

        if(nucCount > currentCount) {
            polymerMap.put(nuc, nucCount);
        }
    }

    /**
     * Determines the longest homopolymer length out of the function calculateMaxHomopolymerLength
     *
     * @param nucs The individual nucleotide map
     * @return Returns a Pair (Hashmap with only one entry, one key and value; a 'pair') that contains the longest
     * homopolymer length and the corresponding nucleotide
     */
    public Pair<Nucleotide, Integer> getLongestMaxHomopolymerLength(Nucleotide[] nucs) {
        HashMap<Nucleotide, Integer> maxHomopolyLength = calculateMaxHomopolymerLength(nucs);
        LinkedHashMap<Nucleotide, Integer> sorted = sortByValue(maxHomopolyLength, false);
        Map.Entry<Nucleotide, Integer> highest = sorted.entrySet().iterator().next();
        return new Pair<>(highest.getKey(), highest.getValue());
    }

    // Function to sort the MaxHomopolymerLength hashmap from highest value to lowest
    private static <T extends Comparable<? super T>> LinkedHashMap<T, Integer> sortByValue(Map<T, Integer> unsortMap, final boolean order) {
        List<Map.Entry<T, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    /**
     * Calculates the Intermolecular identity of two primers
     * Intermolecular identity is the number of nucleotides that match to the two primers
     *
     * @param pA primer A (forward primer)
     * @param pB primer B (reverse primer)
     * @return Returns the number of nucleotides that matches to the end of the other primer
     *
     */
    public int calculateIntermolecularIdentity(Primer pA, Primer pB) {
        Nucleotide[] aNucleotides = pA.getNucleotides();
        // if entered correctly, primer B is already reverse
        Nucleotide[] bNucleotides = pB.getNucleotides();

        int smallestArr = aNucleotides.length;
        if(bNucleotides.length < aNucleotides.length) {
            smallestArr = bNucleotides.length;
        }

        boolean found = false;
        int shift = 0;
        while(shift < smallestArr && !found) {
            found = true;

            for(int i = 0; i < smallestArr - shift; i++) {
                Nucleotide a = aNucleotides[i+shift];
                Nucleotide b = bNucleotides[i];

                if(!a.pairsWith(b)) {
                    found = false;
                }
            }
            shift++;
        }

        if(found) {
            return smallestArr - shift + 1;
        } else {
            return 0;
        }
    }

    /**
     * Calculates the Intramolecular identity of two primers
     * Intramolecular identity is the number of nucleotides that match to the two primers where the second primer
     * is the initial primer is itself but in reverse
     *
     * Uses the calculation logic of intermolecular identity where the second primer is the initial primer in reverse
     *
     * @param p the forward primer
     * @return Returns the number of nucleotides that matches to the end of the reverse primer
     *
     */
    public int calculateIntramolecularIdentity(Primer p) {
        return this.calculateIntermolecularIdentity(p, p.getReverse());
    }

}
