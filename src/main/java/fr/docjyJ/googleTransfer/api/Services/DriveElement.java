package fr.docjyJ.googleTransfer.api.Services;

import com.google.api.services.drive.Drive;
import fr.docjyJ.googleTransfer.api.Utils.GoogleTransfer;

public class DriveElement extends GoogleTransfer<Drive> {
    //ELEMENT

    //CONSTRUCTOR
    public DriveElement(Drive drive) {
        super(drive);
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

}
