package com.phisa.Schedular;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.filefilter.RegexFileFilter;


public class Schedular {
    //File directory = new File("./src/main/resources");
    //String pattern = "[tT]est[1-2].txt";

    /**
     * Display all files
     * @param files
     */
    public void displayFiles(File[] files){
        for(File file : files) {
            System.out.println(file.getName());
        }
    }

    /**
     * Takes in pattern and path and return filtered file array
     * @param pattern
     * @param path
     * @return file array
     */
    public File[] findFilesByPattern(String pattern, String path){
        FileFilter filter = new RegexFileFilter(pattern);
        File directory = new File(path);
        File[] files = directory.listFiles(filter);
        return files;
    }

    /**
     * Delete files from file array
     * @param files
     */
    public void deleteFiles(File[] files){
        for (File file : files){
            file.delete();
        }
    }


}