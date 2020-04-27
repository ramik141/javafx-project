
// Create new package for this
package com.company.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {

    private String filePath;

    public FileHandler(String filePath) {
        setFilePath(filePath);
    }

    public FileHandler() {}

    // create getter and setter for filePath!
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public String getFilePath(){
        return filePath;
    }


    // Opens the given file and returns the content, uses filePath
    public String openFile() throws Exception{

        String content = "";

        try {
            content = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new IOException("Can't open a file!");
            //System.out.println("problem with IO");
            //e.printStackTrace();
        }
        return content;
    }

    // Saves the content to given file path
    public void saveFile(String content) throws Exception{

        try {
            Files.writeString(Paths.get(filePath), content);
        } catch(IOException e) {
            throw new IOException("Can't save a file!");
            //System.out.println("problem with IO");
            //e.printStackTrace();
        }
    }
}
