package Utilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class FileWorker {
    static Scanner getScanner(Path path) throws IOException, IllegalArgumentException {
        if (Files.exists(path) && !Files.isDirectory(path) && Files.isReadable(path)) {
            return new Scanner(path);
        }
        else throw new IllegalStateException("Bad file given: " + path.toString());
    }

    static BufferedWriter getWriter(Path path) throws IOException, IllegalArgumentException {
        if (Files.exists(path) && !Files.isDirectory(path) && Files.isWritable(path)) {
            return Files.newBufferedWriter(path, StandardOpenOption.WRITE);
        }
        else throw new IllegalStateException("Bad file given: " + path.toString());
    }

    static Path createFile(String name) throws IOException { //deletes existing files
        Path path = Paths.get(name);
        if (!Files.isDirectory(path)) {
            Files.deleteIfExists(path);
            return Files.createFile(Paths.get(name));
        }
        else throw new IllegalStateException("Directory given: " + path.toString());
    }
}
