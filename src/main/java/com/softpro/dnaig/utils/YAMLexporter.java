package com.softpro.dnaig.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class YAMLexporter {

    private static StringBuilder sb;

    public static void initExporter(){
        sb = new StringBuilder();
        sb.append("Scene:\n");
    }

    public static void append(String s){
        sb.append(s);
        sb.append("\n");
    }


    public static void export(File file){
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error while writing YAML file");
        }
    }
}
