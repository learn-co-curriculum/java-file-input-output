# File Input Output (IO)

## Learning Goals

- Explain file I/O.
- Read and write files in Java.

## Introduction

So far, when we get input from a user, we are prompting them through the command
line to enter in information. We then provide them an output through the command
line interface as well. This works great - but what if the user wants to run
the program over and over again with the same input? They would have to re-enter
it every time they run the program and go through all of the prompts again and
again. Similarly, what if the user wants to save the output to look back on?
They would have to either take a screenshot of the output or copy and paste the
output from the command line into a file.

A file. That is a good point. Maybe we can just read and write input and output
from a file!

Computers use files to store information so that the data in those files can be
saved and persisted. Let's see how Java can work with files to do just that when
we run our programs.

## File System Definitions

There are a few definitions and clarifications we need to go over before we
dive into any Java code. These will have to do with a file and how it is
stored in the computer.

In this lesson, we will be working with **text files**. A text file is simply
that - a file that just contains some text in a human-readable format. Text
files usually also have an extension of `.txt` at the end.

Every file is associated with a **path**, which is a location where the file is
stored. For example, if we open up a file explorer, we might see we have some
documents stored in a "Documents" folder. Maybe we are interested in finding a
file called "homework" in the "Documents" folder. In order to get to one of those
documents, we need to specify its path. This could look like this:

```plaintext
/home/flatironschool/Documents/homework.txt
```

Notice that the path lists out each folder or **directory** that the computer
would need to go into in order to find the document we are interested in. This
type of path specifically is called an **absolute path**, meaning it is the
specific location in the file system. It is also sometimes called the full path.

A **relative path** is a path that points to a location relative to the current
directory we are in. For example, if we are sitting in the
`/home/flatironschool` directory, then the relative path for us to get to the
"homework" file is:

```plaintext
./Documents/homework.txt
```

The dot before the path says that we are in the directory that contains the
"Documents" directory. Therefore, to access the "homework" file, we would only
need to go into the "Documents" directory from where we are at currently in the
file system.

## Working with Files in Java

In Java, we will be making use of the `File` class that is in the `java.io`
package. To construct a `File` object, we will need to specify a pathname:

```java
import java.io.File;

public class FileIOMain {
    public static void main(String[] args) {
        File file = new File("simple.txt");
    }
}
```

As we can see, we are specifying a relative path. But we could also specify an
absolute path if we wanted to do so.

The `File` class has several methods we could use, but for now we will move onto
writing to a file in Java. See the
[Java 11 File Documentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/File.html)
for more information on the `File` class and its methods.

### Writing to a File

In order to write to a file, Java has provided a `FileWriter` class. There are
a few ways to construct the `FileWriter` instance. Consider the first two
constructors:

```java
import java.io.File;
import java.io.FileWriter;

public class FileIOMain {
    public static void main(String[] args) {
        String fileName = "simple.txt";
        File file = new File(filename);

        // Pass in the String with the file name
        FileWriter fileWriter1 = new FileWriter(fileName);
        
        // Pass in the File object itself
        FileWriter fileWriter2 = new FileWriter(file);
    }
}
```

To instantiate a `FileWriter` object, we can either pass in a `String` object
with the file name or we can pass in the actual `File` object itself. There is
one issue with our code above and that is that the `FileWriter` constructors
throw an `IOException`. This type of exception signals that an input/output
(I/O) error has occurred. We'll modify the above code to wrap it in a try-catch
to gracefully handle the exception momentarily.

Now that we have our `FileWriter` instance instantiated, let's actually write to
the file. The `FileWriter` class is a subclass of a couple of different classes,
and one of the methods it inherits is called `write()`:

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileIOMain {
    public static void main(String[] args) {
        File file = new File("simple.txt");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("example of writing to a file.");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
```

Before we show the output of this code, let us break down a few things.

As we said before, the `FileWriter` constructors could throw an `IOException`
for a few reasons:

- The file provided is actually a directory instead of a regular file.
- The file does not exist and cannot be created.
- The file cannot be opened for some other reason.

With this being said, we wrapped it in a try-catch. But this try-catch looks a
little different. This is because we used a **try-with-resources** statement
instead of the usual try-catch we have seen before! According to the official
documentation, "the try-with-resources statement is a try statement that
declares one or more resources. A resource is an object that must be closed
after the program is finished with it. The try-with-resources statement ensures
that each resource is closed at the end of the statement." When we instantiate
a `FileWriter` instance, we are opening a resource. This means, we can use the
`FileWriter` object to write to a file as much as we want, but we must tell Java
when we are done using it by calling a `close()` method. The try-with-resources
will actually close the resource for us when it exits the try-catch logic.

Once we are in the `try` block, we can use the `FileWriter` object,
`fileWriter`, that we instantiated to call the `write()` method. This should
then write the text "example of writing to a file" to the `File` object we
specified.

If we were to ever enter the `catch` block, we will just print the stack trace
to the console by calling the method `printStackTrace()`.

Now let's see what happens when we run this program.

Since we provided the `FileWriter` object with a `File`, it will first look to
see if the file exists. Since it doesn't currently exist, it creates the file
using the file path we specified when creating the `File` object. Because we
provided the `File` object a relative path, it will create the file `simple.txt`
in the base directory from which we are running the class:

![create-simple-text-file](https://curriculum-content.s3.amazonaws.com/java-mod-3/file-input-output/create-text-file.png)

Once the `FileWriter` has a file to write to, it will write the text that we
passed into the `write()` method. So if we open this file, it will reveal the
text we wrote in the program:

![simple-text-file-content](https://curriculum-content.s3.amazonaws.com/java-mod-3/file-input-output/write-to-file-content.png)

If we want to append even more text to our `simple.txt` file, we could re-write
our code to this:

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileIOMain {
    public static void main(String[] args) {
        File file = new File("simple.txt");

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(" Hello world!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
```

Notice that in order to append text to the end of a file, we can use a different
`FileWriter` constructor that takes in a `File` object (or a `String` with the
file name) and a `boolean` to tell it whether to write to the end of the file.

If we were to run this, then our `simple.txt` file would have this content:

```plaintext
example of writing to a file. Hello World!
```

Now what if we want to write a list out to our file? Let's modify our code
again to see how to do just that!

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileIOMain {
  public static void main(String[] args) {
    File file = new File("simple.txt");
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
```

**Note**: when we run this code, it will overwrite what we already have in our
`simple.txt` file. When writing to a file, remember that if there is anything
important in this file that you might want to save, to save it off somewhere
else.

Now if we run this, the content of the `simple.txt` file will look like this:

```plaintext
Leslie
Ron
Ann

```

Notice also in the above code that to get the `names` to _not_ print out on the
same line, we had to add the escape sequence `"\n"`.

### Reading from a File

Now, let's keep this new file where it was saved by our program and modify our
`FileIOMain` class so that it no longer writes to the file, but instead reads
from it and displays the text inside the file on the console.

For this, we will use the `File` class alongside the `Scanner` class we used in
earlier sections. Except this time instead of asking our `Scanner` object to
read from the command line, we will ask it to read from the `File` object we
have created:

```java
import java.io.File;
import java.util.Scanner;

public class FileIOMain {
    public static void main(String[] args) {
        String content = "";
        File file = new File("simple.txt");
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            content = content.concat(reader.nextLine());
            content = content.concat("\n");
        }

        System.out.println(content);
    }
}
```

As we can see in the code above, instead of passing `System.in` as an argument
to the `Scanner` constructor, we pass in our `File` object, `file`. This will
scan and read the file instead of user input from a command line! We can then
go through the file line-by-line and parse and read through the content. In this
case, we are just going to read the file and print it out to the console.

Now, as we have seen when using the `Scanner` object, it could throw an
exception. In the past, we have seen it throw a `InputMisMatchException` if the
user entered a String, and we were expecting an integer. In this case, we could
end up with an `IOException` again if the file doesn't exist, or we cannot read
the file for some reason. It is best to wrap this in a try-catch and use the
try-with-resources, so we can close our `Scanner` object when we are finished
reading through the file:

```java
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileIOMain {
    public static void main(String[] args) {
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
```

Also note in the code above the `"\n"` escape sequence is back - this is so we
can print out and read the content as it is formatted in our `simple.txt` file.

Finally, let's run this program and see if it read the `simple.txt` file
correctly! The output for this program is:

```plaintext
Leslie
Ron
Ann

```

### File IO Solutions

It is important to know that there are **many** ways to read and write to a file
in Java. Some of these ways may not make sense to us yet, as we have yet to
cover a topic called "streams". Other ways would use the `java.nio` package
instead of the `java.io` package. We won't go into the differences between the
two packages here, but consider this alternative to reading from a file using
the `Path`, `Paths`, and `Files` class in the `java.nio` package:

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIOMain {

    public static void main(String[] args) {
        Path path = Paths.get("simple.txt");
        try {
            String content = Files.readString(path);

            System.out.println(content);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
```

The first line in the `main()` method specifies the file path in a similar way
to the `File file = new File(String pathname)`. We then feed the static
method `readString()` the `Path` object. The `readString()` method will then
read all the content from the file into a `String` and then close the file once
all the content has been read. Believe it or not, this solution of reading from
a file will give us the same output as our solution before when we used the
`Scanner` object!

For more information on these `java.nio` classes, please see the documentation
below. When it comes to the labs following this lesson, feel free to use any
solution that best fits the problem as long as it meets the requirements.

## References

- [Java 11 File](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/File.html)
- [Java 11 FileWriter](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/FileWriter.html)
- [Java try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html#:~:text=The%20try%20%2Dwith%2Dresources%20statement%20is%20a%20try%20statement%20that,the%20end%20of%20the%20statement.)
- [Java 11 Scanner](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Scanner.html)
- [Java 11 Files](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/Files.html)
- [Java 11 Path](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/Path.html)
- [Java 11 Paths](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/Paths.html)