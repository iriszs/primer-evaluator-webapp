package nl.bioinf.imgorter.primer_evaluator.model;

import nl.bioinf.imgorter.primer_evaluator.model.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileInputHandler {

    private final String inputfile;


    public FileInputHandler(String file) {
        this.inputfile = file;
    }

    /**
     * Opens the file, reads the contents and returns the lines.
     *
     * @return Returns a String containing the text in the file
     * Where every line is on a new line so it can be split later on \n
     */
    @Nullable
    public String readFile() {

        try {
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);
            String currentLine;

            StringBuilder inputText = new StringBuilder();
            while ((currentLine = br.readLine()) != null) {
                inputText.append(currentLine + System.getProperty("line.separator"));

            }

            fr.close();
            br.close();

            return inputText.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
