package io.codeforall.bootcamp.utilities.imports;

import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

public class Importer {

    public static HashMap<String, Picture[]> importFolder(String mainPath) {
        return importFolder(mainPath, ".png");
    }

    public static HashMap<String, Picture[]> importFolder(String mainPath, String fileEnding) {
        HashMap<String, Picture[]> out = new HashMap<>();

        Stream<String> paths = getFolderNames(mainPath);

        for (String subFolder : paths.toList()) {

            String fullPath = mainPath + "/" + subFolder;
            Picture[]pics = importOneFolder(fullPath, fileEnding);

            out.put(subFolder, pics);
        }
        return out;
    }

    public static Picture[] importOneFolder(String path) {
        return importOneFolder(path, ".png");
    }

    public static Picture[] importOneFolder(String path, String fileEnding) {
        long length = count(path);

        Picture[] pics = new Picture[(int) length];

        for (int i=0; i<length; i++) {
            pics[i] = new Picture(0, 0, path + "/" + i + fileEnding);
        }
        return pics;
    }

    public static long count(String folderPath) {
        try (Stream<Path> paths = Files.list(Paths.get(folderPath))) {
            return paths.count();
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Error occurred, return -1 as indication
        }
    }

    public static Stream<String> getFolderNames(String folderPath) {
        try {
            // Get names of all subfolders
            Stream<String> subfolderNames = getSubfolderNames(folderPath);
            return subfolderNames;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static String[] subfolderNames(String filePath) {
        Stream<String> subFolders = getFolderNames(filePath);

        return getFolderNames(filePath).toArray(String[]::new);
    }

    public static Stream<String> getSubfolderNames(String folderPath) throws IOException {
        Path mainFolderPath = Paths.get(folderPath);
        return Files.walk(Paths.get(folderPath))
                .filter(path -> Files.isDirectory(path) && !path.equals(Paths.get(folderPath)))
                .map(path -> mainFolderPath.relativize(path).toString());
    }



}
