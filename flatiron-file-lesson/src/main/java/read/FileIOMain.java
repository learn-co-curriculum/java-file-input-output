package read;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileIOMain {
    public static void main(String[] args) throws IOException {
        String content = "";
        File file = new File("simple.txt");
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                content = content.concat(reader.nextLine());
                content = content.concat("\n");
            }

            System.out.println(content);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
