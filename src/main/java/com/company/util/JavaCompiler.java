package com.company.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class JavaCompiler {
    public static String compileAndRun(String file) {
        String result = "";
        String fileName = file.substring(file.lastIndexOf("\\") + 1);
        String folderPath = file.replace(fileName, "");
        System.out.println(file);


        try {
            String[] compile = {"javac", fileName};
            String [] run = {"java", "-cp", folderPath, file};
            // Käännös
            var process = Runtime.getRuntime().exec(compile);
            int fc = process.getErrorStream().read();
            if( fc != -1 ) {
                System.out.print((char) fc);
                String data = getOutput(process.getErrorStream());
                System.out.println(data);
                result += data;
            }
            int value = process.waitFor();
            // Ajo
            if(value == 0) {
                var runProcess = Runtime.getRuntime().exec(run);
                int fcs = runProcess.getErrorStream().read();
                if(fcs != -1) {
                    System.out.print((char) fcs);
                    String data = getOutput(runProcess.getErrorStream());
                    System.out.println(data);
                    result += data;
                } else {
                    String data = getOutput(runProcess.getInputStream());
                    System.out.println(data);
                    result += data;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
    public static String getOutput(InputStream stream) throws IOException {
        String result = "";
        try(BufferedReader bufferedReader
                    = new BufferedReader(new InputStreamReader(stream))) {
            result = bufferedReader.lines().collect(Collectors.joining("\n"));
        }
        return result;
    }

}
