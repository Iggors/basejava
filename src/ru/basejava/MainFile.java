package ru.basejava;

import java.io.File;

public class MainFile {
    private static String counter = "|-";

    public static void main(String[] args) {
        String path = "../basejava/src";
        File dir = new File(path);
        System.out.println(dir.getName());
        getSubDirectory(dir);
    }

    private static void getSubDirectory(File dir) {
        File[] children = dir.listFiles();

        if (children != null) {
            for (File child : children) {
                if (child.isFile()) {
                    System.out.println(counter + "File: " + child.getName());
                } else if (child.isDirectory()) {
                    System.out.println(counter + "Directory: " + child.getName());
                    counter += '-';
                    getSubDirectory(child);
                }
            }
            counter = counter.substring(0, counter.length() - 1);
        }

    }
}
