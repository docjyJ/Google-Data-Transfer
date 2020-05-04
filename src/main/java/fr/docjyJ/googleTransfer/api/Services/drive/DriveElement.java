package fr.docjyJ.googleTransfer.api.Services.drive;

import com.google.api.services.drive.Drive;
import fr.docjyJ.googleTransfer.api.Utils.GoogleTransfer;

public class DriveElement extends GoogleTransfer {
    //ELEMENT
    protected transient Drive service;

    //CONSTRUCTOR
    public DriveElement(Drive service) {
        this.service = service;
    }

    //READ
    public DriveElement readAll() {
        return this;
    }

    //PUT
    public DriveElement putAll(DriveElement data) {
        return this;
    }

    //GET
    public Drive getService() {
        return service;
    }

}
