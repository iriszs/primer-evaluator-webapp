package nl.bioinf.imgorter.primer_evaluator.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO CHECK IF FILE DOES NOT CONTAIN MORE THAN TWO LINES

public class FileInputHandler {

    private final String inputfile;


    public FileInputHandler(String file) {
        this.inputfile = file;
    }

    /**
     * Opens the file, reads the contents and returns the lines.
     *
     * @return Returns a String containing the text in the file
     */
    public StringBuilder readFile() throws FileNotFoundException {
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder inputText = new StringBuilder();
        //String inputText = null;


        try {
            fr = new FileReader(inputfile);
            br = new BufferedReader(fr);
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                inputText.append(currentLine + System.getProperty("line.separator"));

            }

        }
        catch (IOException e) {

            e.printStackTrace();

        }
        finally {
            try {
                if (br != null){
                    br.close();
                    fr.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return inputText;
    }

}
