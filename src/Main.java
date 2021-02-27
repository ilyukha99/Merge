import Utilities.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Pair<List<String>, List<String>> info = ArgParser.parse(args);
            new Worker().start(Validator.validate(info));
        }
        catch (Exception exc) {
            System.err.println(exc.getMessage());
        }
    }
}
