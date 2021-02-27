package Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.nio.file.Path;
import java.util.List;

public class Validator {
    public static Pair<Status, ArrayList<Path>> validate(Pair<List<String>, List<String>> info)
            throws RuntimeException, IOException {
        List<String> params = info.getFirst();
        if (params.contains("-i") && params.contains("-s")) {
            throw new IllegalArgumentException("Either -s or -i can be used.");
        }

        if (params.contains("-a") && params.contains("-d")) {
            throw new IllegalArgumentException("Either -a or -d can be used.");
        }

        if (!params.contains("-s") && !params.contains("-i")) {
            throw new IllegalArgumentException("There must be either -s or -i flag.");
        }

        Status status = Status.NONE;
        if (params.contains("-d")) {
            if (params.contains("-i")) {
                status = Status.INT_DESC;
            }
            if (params.contains("-s")) {
                status = Status.STRING_DESC;
            }
        }
        else {
            if (params.contains("-i")) {
                status = Status.INT_ASC;
            }
            if (params.contains("-s")) {
                status = Status.STRING_ASC;
            }
        }
        if (status == Status.NONE) {
            throw new IllegalStateException("An argument error");
        }

        List<String> fileNames = info.getSecond();
        if (fileNames.size() < 2) {
            throw new IllegalArgumentException("Too few file names found. " +
                    "At least one input and one output file required.");
        }

        ArrayList<Path> paths = new ArrayList<>();
        FileWorker.createFile(fileNames.get(0)); //output file
        for (String name : fileNames) {
            Path path = Paths.get(name);
            if (Files.exists(path)) {
                paths.add(path);
            }
            else throw new IllegalArgumentException("File " + path.toString() + " does not exist.");
        }

        return new Pair<>(status, paths);
    }
}
