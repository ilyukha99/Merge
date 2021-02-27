package Utilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Function;

public class Worker {
    private int flag; //asc == 1, desc == -1
    private int index;
    private final ArrayList<Path> createdFiles = new ArrayList<>();

    public void start(Pair<Status, ArrayList<Path>> info) throws IOException {
        int value = info.getFirst().ordinal(); //value %2 == 0 ? desc : asc, val > 2 ? int : str
        flag = (value % 2 == 0) ? -1 : 1;

        if (value > 2) {
            mergeAllFiles(info.getSecond(), Integer::compare, Integer::parseInt);
        }
        else mergeAllFiles(info.getSecond(), String::compareTo, Function.identity());
    }

    private <T extends Comparable<T>> void mergeAllFiles(ArrayList<Path> files, Comparator<T> comparator,
                                                      Function<String, T> converter) throws IOException {

        Path outputFile = files.get(0);
        files.remove(0);

        ArrayList<Path> newFiles = new ArrayList<>(), tmp;
        int size;
        while (files.size() != 1) {
            size = files.size();
            for (int it = 0; it < size - 1; it+=2) { //it % 2 == 0
                newFiles.add(mergeTwoFiles(files.get(it), files.get(it + 1), comparator, converter));
            }
            if (size % 2 == 1) {
                newFiles.add(files.get(size - 1));
            }

            for (Path file : createdFiles) {
                if (!newFiles.contains(file)) {
                    Files.deleteIfExists(file);
                }
            }
            files.clear();
            tmp = newFiles;
            newFiles = files;
            files = tmp;
        }

        Files.copy(files.get(0), outputFile, StandardCopyOption.REPLACE_EXISTING);
        for (Path file : createdFiles) {
            Files.deleteIfExists(file);
        }
    }

    private <T extends Comparable<T>> Path mergeTwoFiles(Path file1, Path file2,Comparator<T> comparator,
                                                         Function<String, T> converter) throws IOException {

        Scanner scanner1 = FileWorker.getScanner(file1);
        Scanner scanner2 = FileWorker.getScanner(file2);

        if (!(scanner1.hasNext() || scanner2.hasNext()) || (scanner1.hasNext() && !scanner2.hasNext())) {
            scanner1.close();
            scanner2.close();
            return file1;
        }

        if (!scanner1.hasNext()) {
            scanner1.close();
            scanner2.close();
            return file2;
        }

        Path outputFilePath = FileWorker.createFile("temp" + index++);
        createdFiles.add(outputFilePath);
        BufferedWriter writer = FileWorker.getWriter(outputFilePath);
        
        T val1 = converter.apply(scanner1.nextLine());
        T val2 = converter.apply(scanner2.nextLine());
        while (scanner1.hasNextLine() && scanner2.hasNextLine()) {
            if (comparator.compare(val1, val2) * flag > 0) {
                writer.write(val2.toString());
                val2 = converter.apply(scanner2.nextLine());
            } else {
                writer.write(val1.toString());
                val1 = converter.apply(scanner1.nextLine());
            }
            writer.newLine();
        }

        if (!scanner1.hasNextLine() && scanner2.hasNextLine()) { //val1 is the last line from file1
            finishMerge(scanner2, writer, val2, val1, flag, comparator, converter);
        }

        else if (scanner1.hasNextLine()) { //val2 is the last line from file2
            finishMerge(scanner1, writer, val1, val2, flag, comparator, converter);
        }

        writer.close();
        scanner1.close();
        scanner2.close();
        return outputFilePath;
    }

    private <T> void finishMerge(Scanner scanner, BufferedWriter writer, T cur, T other, int flag,
                                 Comparator<T> comparator, Function<String, T> converter) throws IOException {
        boolean finished = false;
        while (scanner.hasNextLine()) {
            if (comparator.compare(cur, other) * flag > 0) {
                writer.write(other.toString());
                writer.newLine();
                writer.write(cur.toString());
                writer.newLine();
                while (scanner.hasNextLine()) {
                    writer.write(scanner.nextLine());
                    writer.newLine();
                }
                finished = true;
            }
            else {
                writer.write(cur.toString());
                writer.newLine();
                cur = converter.apply(scanner.nextLine());
            }
        }
        if (!finished) {
            if (comparator.compare(cur, other) * flag > 0) {
                writer.write(other.toString());
                writer.newLine();
                writer.write(cur.toString());
            }
            else {
                writer.write(cur.toString());
                writer.newLine();
                writer.write(other.toString());
            }
        }
    }
}
