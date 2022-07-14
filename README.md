# File Input Output (IO)

## Learning Goals

- Explain file I/O
- Read and write files in Java

## Introduction

So far, we have worked with data structures that allow us to store information
while our program is running. This is helpful for a lot of scenarios, some of
which we have covered. There is an obvious limitation with all the work we've
done thus far, however, and that is that once our program stops running, all the
data associated with it is lost. If we run our program again, we have to start
from the beginning and provide all the user input again and go through all the
same logic and calculations as before.

The process of saving your program's data so it can be used later is called
"data persistence". There are many ways to implement data persistence - in this
section, we will focus on file-based data persistence.

## Reading from and writing to a file

The JDK provides classes for interfacing with files. Let's start with a simple
program that uses the `FileWriter` class:

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileIORunner {
    public static void main(String[] args) throws IOException {
        writeToFile("simple.data", "example of writing data to file");
    }

    static void writeToFile(String fileName, String text) throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write("example of writing data to file");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (fileWriter != null)
                fileWriter.close();
        }
    }
}
```

> Note: there are aspects of this implementation that we need to clarify, which
> we will do a further below, once we have set up the read/write logic.

You can run this class, and you will see a new file named `simple.data` in the
base directory from which you are running the class:

![simple-data-file.png](https://curriculum-content.s3.amazonaws.com/java-mod-3/simple-data-file.png)

Opening this file will reveal the text that you wrote to file in your program:

![simple-data-content.png](https://curriculum-content.s3.amazonaws.com/java-mod-3/simple-data-content.png)

Now, let's keep this new file where it was saved by our program and modify our
runner class so that it no longer writes to the file, but instead reads from it
and displays the text inside the file on the console.

For this, we will use the `File` class alongside the `Scanner` class we used in
earlier sections. Except this time instead of asking our `Scanner` object to
read from the command line, we will ask it to read from the `File` object we
have created:

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileIORunner {
    public static void main(String[] args) throws IOException {
//        writeToFile("simple.data", "example of writing data to file");
        System.out.println("Restored the following from simple.data: ");
        System.out.println(readFromFile("simple.data", true));
    }

    static String readFromFile(String fileName, boolean addNewLine) throws IOException {
        String returnString = new String();
        Scanner fileReader = null;
        try {
            File myFile = new File(fileName);
            fileReader = new Scanner(myFile);
            while (fileReader.hasNextLine()) {
                returnString += fileReader.nextLine();
                if (addNewLine)
                    returnString += "\n";
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (fileReader != null)
                fileReader.close();
        }

        return returnString;
    }

    static void writeToFile(String fileName, String text) throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write(text);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (fileWriter != null)
                fileWriter.close();
        }
    }
}
```

Running this program, you will see that you can read the content of your file
even though you're not writing to that file from this version of the program.
Your string was persisted from your previous run.

Furthermore, you can now also modify the content of your file, and you will see
that content output to the console when you run your program again.

Let's break down both the writing and reading logic, as we have introduced a few
new concepts in this code:

- `IOException`: when dealing with any type of system resources, exceptions may
  happen when files are not present or not available (because they're open by
  other programs for example), so we should always have our IO code in `try` and
  `catch` blocks
- `finally` block: a `finally` block can be added to any `try` and `catch`
  block, but it's not mandatory, which is why we haven't seen it until now. A
  `finally` block's code is guaranteed to run regardless of what happens during
  the `try` and `catch` block:
  - If an exception occurs in the `try` block, the code inside the `catch` block
    will get executed, and then the code inside the `finally` block gets
    executed.
  - If no exception occurs in the `try` block, the code inside the `catch` block
    doesn't get executed, and then the code inside the `finally` block gets
    executed.
- Calling the `close()` method: when dealing with IO resources, we must ensure
  that our program releases those resources before it's done, which is why the
  call to the `close()` method is inside the `finally` block for both the
  `read()` and `write()` methods. This complicates things a bit, however,
  because the `close()` methods also throw `IOException` exceptions, so those
  exceptions either have to be caught or they have to be bubbled back to the
  caller. Since there is nothing we can do about the exception if it happens in
  the `finally` block, we simply specific that our methods `throws IOException`
  and we let it bubble up to the caller.
- Since the objects we need to close the `close()` methods on need to be defined
  outside the `try` block, so they can be in scope for the `finally` block, we
  have to define them globally in the method and initialize them to `null`
  because the call to initialize them potentially throws an `IOException`. This
  means that by the time we get to the `finally` block, the IO objects may still
  be `null`, so we must check for `null` before we call the `close()` method.
- "\n" is a special character that means "line return", so this line:
  `returnString += fileReader.nextLine() + "\n";` appends the next line in the
  `fileReader` to the `returnString` and adds a new line character at the end.
- We make the logic to add the new line character ("\n") conditional, so that we
  can re-use this method when we don't want a new line character

Now that we have the ability to read and write to file, the question becomes:
how can we use this new functionality to restore useful data to our program when
it starts? The functionality we've implemented so far lets us read simple
strings from the file we're opening, but we don't have a mechanism to match them
to specific fields in our class.
