package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Searcher {
    private static Searcher instance;

    private Searcher() {

    }

    public static Searcher getInstance() {
        if (instance == null) {
            instance = new Searcher();
        }
        return instance;
    }

    public static void searchFilesWithGivenFormat(String dirToSearchIn, String textFormat, List<File> foundFiles) {
        File folder = new File(dirToSearchIn);

        if (folder.listFiles() != null) {
            File[] files = folder.listFiles();
            for(File curFile : files) {
                if(curFile.isDirectory()) {
                    String currentDir = dirToSearchIn.concat("\\").concat(curFile.getName());
                    searchFilesWithGivenFormat(currentDir, textFormat, foundFiles);
                }
                if(checkFormat(curFile.getName(), textFormat)) {
                    //System.out.println(curFile);
                    foundFiles.add(curFile);
                }
            }
        }
    }

    public static boolean checkFormat(String str, String format) {
        String extension = "";

        int i = str.lastIndexOf('.');
        int p = Math.max(str.lastIndexOf('/'), str.lastIndexOf('\\'));

        if (i > p) {
            extension = str.substring(i+1);
        }

        return extension.equals(format);
    }

    public List<File> searchFilesWithGivenText(List<File> files, String textToSearch) {
        List<File> foundFilesWithGivenText = new ArrayList<>();
        for (File f : files) {
            try {
                FileReader fileIn = new FileReader(f);
                BufferedReader reader = new BufferedReader(fileIn);
                String line;
                while ((line = reader.readLine()) != null) {
                    if ((line.contains(textToSearch))) {
                        foundFilesWithGivenText.add(f);
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return foundFilesWithGivenText;
    }


}
