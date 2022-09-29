package write;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileIOMain {
    public static void main(String[] args) {
        File file = new File("simple.txt");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("example of writing to a file.");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(" Hello World!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        List<String> names = new ArrayList<String>(Arrays.asList(
                "Leslie",
                "Ron",
                "Ann"
        ));

        try (FileWriter fileWriter = new FileWriter(file)) {
            for (String name : names) {
                fileWriter.write(name + "\n");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
