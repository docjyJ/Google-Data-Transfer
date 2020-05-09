package fr.docjyJ.googleTransfer.api.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.*;

public class GoogleTransfer {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    transient File file;

    public String toJson() {
        return GSON.toJson(this);
    }
    public GoogleTransfer setFile(String fileName) {
        this.file = new File(fileName);
        return this;
    }
    public GoogleTransfer open() throws IOException {
        if(this.file.exists())
            Desktop.getDesktop().open(this.file);
        return this;
    }
    public GoogleTransfer generate() throws IOException {
        FileWriter writer = new FileWriter(this.file);
        writer.write(this.toJson());
        writer.close();
        return this;
    }
    public GoogleTransfer print() {
        logPrint(this);
        logPrint(this.toJson());
        return this;
    }
    public static void logPrint(Object object){
        System.out.println(object);
    }
    public static void logPrint(String methode, String type, String element){
        System.out.println(
                methode+": "+type+"-["+element+"]"
        );
    }
    public static void logPrint(String methode, String type, String element, String element2){
        System.out.println(
                methode+": "+type+"-["+element+"]-["+element2+"]"
        );
    }

}
