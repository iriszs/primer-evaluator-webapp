package nl.bioinf.imgorter.primer_evaluator.model;

import java.util.HashMap;

public class Calculator {

    /**
     * Counts the number of nucleotides in the given primer
     *
     * @param nucleotides Map of individual nucleotides (sequence without 5'- and 3-') to calculate from
     * @return Returns a HashMap where the key is the counted nucleotide and the value the occurrence count
     */
    public HashMap<Nucleotide, Integer> CountNucleotides(Nucleotide[] nucleotides){
        System.out.println("In the calculate nucleotides");
        System.out.println(nucleotides);
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
        System.out.println("basecount map" + nucCount);

        double GCPercentage;
        int countA = nucCount.get(Nucleotide.A);
        int countC = nucCount.get(Nucleotide.C);
        int countT = nucCount.get(Nucleotide.T);
        int countG = nucCount.get(Nucleotide.G);

        System.out.println("Count of A bases: " + countA);
        System.out.println("Count of C bases: " + countC);
        System.out.println("Count of T bases: " + countT);
        System.out.println("Count of G bases: " + countG);

        GCPercentage = (double) (countG + countC) / (countA + countC + countG + countT) * 100;

        System.out.println("GCpercentage calculated in the calculator = " + GCPercentage);

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
        System.out.println("melting temp in the function = " + meltingTemp);

        return meltingTemp;
    }

    /**
     * Calculates the maximum homopolymer lenght (longest sequence of continuous nucleotides)
     * For the input "AAACCCGGGTTTTAAAA", A = 4, C = 3, G = 3, T = 4
     *
     * @param nucleotides The individual nucleotide map (base sequence without 5'- and 3-') to calculate from
     * @return Returns HashMap where the key is the counted nucleotide and the value is the continuous occurrence count
     * Returns a HashMap instead of integer to have more information stored to request later
     * The Evaluator class will return the nucleotide with the highest value.
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

        //System.out.println("a Counter = " + aCounter);
        //System.out.println("c Counter = " + cCounter);
        //System.out.println("g Counter = " + gCounter);
        //System.out.println("t Counter = " + tCounter);

        return homopolymerLenghts;
    }

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
