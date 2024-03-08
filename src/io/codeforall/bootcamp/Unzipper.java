package io.codeforall.bootcamp;

import java.io.*;
import java.util.zip.*;

public class Unzipper {
    public static void unzip(String zipFile, String destDirectory) {
        try {
            // Get the input stream for the ZIP file within the JAR
            InputStream inputStream = Unzipper.class.getResourceAsStream("/" + zipFile);
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));

            // Create a directory to extract the files
            File outputDir = new File(destDirectory);
            outputDir.mkdirs();

            // Extract the files from the ZIP file
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().startsWith("__MACOSX")) {
                    continue;
                }
                File entryFile = new File(outputDir, entry.getName());

                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    byte[] buffer = new byte[1024];
                    try (FileOutputStream fos = new FileOutputStream(entryFile);
                         BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length)) {
                        int read;
                        while ((read = zipInputStream.read(buffer, 0, buffer.length)) != -1) {
                            bos.write(buffer, 0, read);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }

            zipInputStream.close();
            System.out.println("ZIP file extracted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unzip() {
        unzip("resources.zip", ".resources");
    }
}