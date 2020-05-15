package fr.docjyJ.googleTransfer.api.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GoogleTransfer<Service> {
    //ELEMENT
    protected transient Service service;

    //CONSTRUCTOR
    public GoogleTransfer(){}
    public GoogleTransfer(Service service) {
        this.service = service;
    }

    //GET
    public Service getService() {
        return service;
    }

    //LOG
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

    //OUTPUT
    transient protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    transient protected File file;
    public String toJson() {
        return GSON.toJson(this);
    }
    public GoogleTransfer<Service> setFile(String fileName) {
        this.file = new File(fileName);
        return this;
    }
    public GoogleTransfer<Service> open() throws IOException {
        if(this.file.exists())
            Desktop.getDesktop().open(this.file);
        return this;
    }
    public GoogleTransfer<Service> generate() throws IOException {
        FileWriter writer = new FileWriter(this.file);
        writer.write(this.toJson());
        writer.close();
        return this;
    }
    public GoogleTransfer<Service> print() {
        logPrint(this);
        logPrint(this.toJson());
        return this;
    }

}
