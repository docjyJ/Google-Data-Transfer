package fr.docjyJ.googleTransfer.api.Utils;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class GoogleTransfer {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected static final String CLIENT_SECRETS = "client_secret.json";
    protected static final String APPLICATION_NAME = "Google Transfer";
    protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    protected static final Collection<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/youtube",
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/photoslibrary",
            "https://www.googleapis.com/auth/gmail.modify",
            "https://www.google.com/m8/feeds/");

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
        this.file.createNewFile();
        FileWriter writer = new FileWriter(this.file);
        writer.write(this.toJson());
        writer.close();
        return this;
    }
    public GoogleTransfer print() {
        logPrintln(this);
        logPrintln(this.toJson());
        return this;
    }
    public void logPrintln(Object object){
        System.out.println(object);
    }
    public void logPrint(Object object){
        System.out.print(object);
    }

}
