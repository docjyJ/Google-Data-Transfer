package fr.docjyJ.googleTransfer.Services.gmail;

import com.google.api.services.gmail.Gmail;
import fr.docjyJ.googleTransfer.Utils.GoogleTransfer;

import java.io.IOException;

public class GmailElement extends GoogleTransfer {
    //ELEMENT
    transient Gmail service;

    //CONSTRUCTOR
    public GmailElement(Gmail service) {
        this.service = service;
    }

    //READ
    public GmailElement readAll() throws IOException {
        return this;
    }

    //PUT
    public GmailElement putAll(Gmail newClient) throws IOException {
        return this;
    }

    //GET
    public Gmail getService() {
        return service;
    }
}
