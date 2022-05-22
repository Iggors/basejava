package ru.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        String filePath = "../basejava";
        File dir = new File(filePath);

        getSubDirectory(dir);
    }

    private static void getSubDirectory(File dir) {
        File[] children = dir.listFiles();

        if (children != null) {
            for (File child : children) {
                if (child.isFile()) {
                    System.out.println("File: " + child.getName());
                } else if (child.isDirectory()) {
                    System.out.println("Directory: " + child.getName());
                    getSubDirectory(child);
                }
            }
        }

    }
}
