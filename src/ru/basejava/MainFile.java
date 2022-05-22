package ru.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        String filePath = "../basejava";
        File dir = new File(filePath);

        getSubDirectory(dir);
    }

    private static void getSubDirectory(File dir) {
        System.out.println(dir.getAbsolutePath());

        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (File child : children) {
                getSubDirectory(child);
            }
        }
    }
}
