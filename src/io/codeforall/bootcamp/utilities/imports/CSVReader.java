package io.codeforall.bootcamp.utilities.imports;

import java.io.*;

public class CSVReader {

    private final String fileName;
    private BufferedReader bufferedReader;
    private FileReader fileReader;
    private String delimiter = ",";

    public CSVReader(String fileName) throws RuntimeException {
        this.fileName = fileName;
        this.fileReader = createFileReader(fileName);
    }

    public CSVReader(String fileName, String delimiter) throws RuntimeException {
        this.fileName = fileName;
        this.delimiter = delimiter;
    }


    private FileReader createFileReader(String fileName) {
        try {
            return new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("CSV Importer object creation failed: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    public String[][] importCsv() {
        String line = "";

        try {
            bufferedReader = new BufferedReader(fileReader);

            int rows = 0;
            int columns = 0;

            while ((line = bufferedReader.readLine()) != null) {
                rows++;
                String[] values = line.split(delimiter);
                columns = Math.max(columns, values.length);
            }

            String[][] data = new String[rows][columns];
            bufferedReader = new BufferedReader(createFileReader(fileName));

            int rowIndex = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(delimiter);
                for (int i = 0; i < values.length; i++) {
                    data[rowIndex][i] = values[i];
                }
                rowIndex++;
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while reading line in file " + fileName + ": " + e.getMessage());
            throw new RuntimeException();
        }
    }
}
