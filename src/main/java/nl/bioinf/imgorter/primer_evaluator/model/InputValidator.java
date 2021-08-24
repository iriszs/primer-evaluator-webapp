package nl.bioinf.imgorter.primer_evaluator.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

    /**
     * Checks if the entered text contains a valid sequence. Checks:
     * - Sequence must start with 5'- or 3'-
     * - End with -5' or -3'
     * - Must end with -3' if starts with 5'-
     * - Must end with -5' if starts with 3'-
     * @param inputSequence
     * @return True if the sequence is valid, false if it is not (if one of the points fail)
     */
    public static boolean checkValidSequence(String inputSequence) {
        String trimmedSeq = inputSequence.trim();
        if(!(trimmedSeq.startsWith("3'-") || trimmedSeq.startsWith("5'-"))) {
            System.out.println("first check false");
            return false;
        }
        if(!(trimmedSeq.endsWith("-3'") || trimmedSeq.endsWith("-5'"))){
            System.out.println("second check false");
            return false;
        }
        if(trimmedSeq.startsWith("-3'") && trimmedSeq.endsWith("-3'")){
            System.out.println("third check false");
            return false;
        }
        if(trimmedSeq.startsWith("-5'") && trimmedSeq.endsWith("-5'")){
            System.out.println("fourth check false");
            return false;
        }

        System.out.println("passed!!!!");
        return true;
    }


    /**
     * Checks if the entered text contains all valid nucleotides.
     * -> Must contain valid nucleotides only (ACTG/actg)
     * @param baseSeq
     * @return True if all nucleotides are valid, false if it is not
     */
    public static boolean checkValidNucleotides(String baseSeq) {

        // Fifth check
        // Sequence can only contain nucleotides in uppercase or lowercase (ACTG/actg)
        Pattern dnaCharPat = Pattern.compile("^[ACGT]*$", Pattern.CASE_INSENSITIVE);
        Matcher dnaCharMat = dnaCharPat.matcher(baseSeq);
        if(!dnaCharMat.matches()) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the entered text contains all valid nucleotides.
     * -> Must contain valid nucleotides only (ACTG/actg)
     * @param baseSequence
     * @return True if all nucleotides are valid, false if it is not
     */
    public static boolean checkValidLength (String baseSequence) {
        if ((baseSequence.length() < 18) || (baseSequence.length() > 22) ){
            return false;
        }
        return true;
    }
}
