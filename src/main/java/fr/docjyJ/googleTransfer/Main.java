package fr.docjyJ.googleTransfer;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static fr.docjyJ.googleTransfer.api.GoogleOauth.*;
import static fr.docjyJ.googleTransfer.api.YouTubeData.*;

public class Main {
    public static void main(String[] args)
            throws GeneralSecurityException, IOException {
        transferLike(getService(),getService(),"like","");
        //YouTubeData.transferLike(getService(),getService(),"dislike","");


    }
}


