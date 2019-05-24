package model.utils.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {

    public static String readFromFile(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = "";
        StringBuilder text = new StringBuilder();
        do {
            text.append(line);
            line = bufferedReader.readLine();

        } while(line != null);

        bufferedReader.close();
        return text.toString();
    }

}
