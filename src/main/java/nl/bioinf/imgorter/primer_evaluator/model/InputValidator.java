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
     * - Must contain valid nucleotides only (ACTG/actg)
     * @param inputSequence
     * @return True if the sequence is valid, false if it is not (if one of the points fail)
     */
    public static boolean isValid(String inputSequence) {
        // First check
        // Must start with 5'- or 3'-
        if(!(inputSequence.startsWith("5'-") || inputSequence.startsWith("3'-"))) {
            return false;
        }

        // Second check
        // Must end with -3' or -5'
        if(!(inputSequence.endsWith("-3'") || inputSequence.endsWith("-5'"))) {
            return false;
        }

        // Third check
        // If sequence starts with 5'-, it must end with -3'
        if(inputSequence.startsWith("5'-") && !inputSequence.endsWith("-3'")) {
            return false;
        }

        // Fourth check
        // If sequence starts with 3'-, it must end with -5'
        if(inputSequence.startsWith("3'-") && !inputSequence.endsWith("-5'")) {
            return false;
        }

        String baseSeq = inputSequence.split("-")[1];

        // Fifth check
        // Sequence can only contain nucleotides in uppercase or lowercase (ACTG/actg)
        Pattern dnaCharPat = Pattern.compile("^[ACGT]*$", Pattern.CASE_INSENSITIVE);
        Matcher dnaCharMat = dnaCharPat.matcher(baseSeq);
        if(!dnaCharMat.matches()) {
            return false;
        }

        return true;
    }
}
