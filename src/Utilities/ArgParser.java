package Utilities;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgParser {
    private static final List<String> options = Arrays.asList("-i", "-s", "-a", "-d");

    public static Pair<List<String>, List<String>> parse(String[] args) {
        List<String> params = Arrays.stream(args)
                .filter(options::contains)
                .collect(Collectors.toList());

        List<String> fileNames = Arrays.stream(args)
                .filter(s -> s.matches(".+..+"))
                .collect(Collectors.toList());
        return new Pair<>(params, fileNames);
    }
}
