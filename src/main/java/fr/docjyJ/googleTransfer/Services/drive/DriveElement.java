package fr.docjyJ.googleTransfer.Services.drive;

import com.google.api.services.drive.Drive;
import fr.docjyJ.googleTransfer.Utils.GoogleTransfer;

import java.io.IOException;

public class DriveElement extends GoogleTransfer {
    //ELEMENT
    protected transient Drive service;

    //CONSTRUCTOR
    public DriveElement(Drive service) {
        this.service = service;
    }

    //READ
    public DriveElement readAll() throws IOException {
        return this;
    }

    //PUT
    public DriveElement putAll(Drive newClient) throws IOException {
        return this;
    }

    //GET
    public Drive getService() {
        return service;
    }

}
