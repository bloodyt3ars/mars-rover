package org.olympiad;

import org.olympiad.model.Answer;
import org.olympiad.model.Map;
import org.olympiad.service.PathResolver;
import org.olympiad.service.PathResolver1;
import org.olympiad.service.Reader;
import org.olympiad.service.Writer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final String FILE_OUTPUT = "answer.json";
    private static final String FILE_INPUT = "src/test/resources/map.json";

    private static final List<String> FILE_INPUTS = Arrays.asList("src/test/01/map.json", "src/test/02/map.json", "src/test/03/map.json", "src/test/04/map.json");

    public static void main(String[] args) throws IOException {
        final String pathToInFile;
        final  List<String> pathsToInFiles = FILE_INPUTS;
        if (args.length == 1) {
            pathToInFile = args[0];
        } else {
            pathToInFile = FILE_INPUT;
        }
        for (int i = 0; i < pathsToInFiles.size(); i++) {
            Reader reader = new Reader();
            Map map = reader.readMap(pathsToInFiles.get(i));
            PathResolver resolver = new PathResolver();
            // найти один из возможных оптимальных путей на карте
            Answer answer = resolver.findAnswer(map);

            //сохранить найденный путь в файл
            Writer writer = new Writer(FILE_OUTPUT, answer);
            writer.write();
        }
        // прочитать карту
/*        Reader reader = new Reader();
        Map map = reader.readMap(pathToInFile);
        PathResolver1 resolver = new PathResolver1();
        // найти один из возможных оптимальных путей на карте
        Answer answer = resolver.findAnswer(map);

        //сохранить найденный путь в файл
        Writer writer = new Writer(FILE_OUTPUT, answer);
        writer.write();*/
    }

}
