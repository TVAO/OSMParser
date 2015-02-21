package Model;

import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Model handles data from text files and stores them in LineObjects
 * Created by TVANO on 07/02/15.
 */
public class Model {

    /**
     * Reads a file and stores LineObjects holding coordinates in obsList used in Controller class
     * Credits for iteration method: Troels Sørensen
     * @param filename - file holding coordinates 
     * @return
     */
    public ObservableList<LineObject> readFile(File filename, ObservableList obsList) {
        // long time = System.nanoTime();

        try (BufferedReader input = new BufferedReader(new FileReader(filename))) {
            for (String line = input.readLine(); line != null; line = input.readLine()) {
                String[] words = line.split(" ");
                obsList.add(new LineObject(
                        Double.parseDouble(words[0]),
                        Double.parseDouble(words[1]),
                        Double.parseDouble(words[2]),
                        Double.parseDouble(words[3])));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        // System.out.printf("Model load time: %d ms\n", // Load time
        //  (System.nanoTime() - time) / 1000000);
        return obsList;
    }
}
