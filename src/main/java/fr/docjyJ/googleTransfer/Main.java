package fr.docjyJ.googleTransfer;

import com.google.api.services.youtube.YouTube;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static fr.docjyJ.googleTransfer.Lang.question;
import static fr.docjyJ.googleTransfer.api.GoogleOauth.*;
import static fr.docjyJ.googleTransfer.api.YouTubeData.*;

public class Main {
    public static void main(String[] args)
            throws GeneralSecurityException, IOException, InterruptedException {
        YouTube compteA = getService(Lang.FIRST_STEP);
        YouTube compteB = getService(Lang.SECOND_STEP);

        if(question("like"))
            transferLike(compteA,compteB,"","like");
        if(question("dislike"))
            transferLike(compteA,compteB,"","dislike");
        if(question("subscriptions"))
            transferSubscriptions(compteA,compteB,"");
        if(question("playlist"))
            transferPlaylist(compteA,compteB,"");
    }
}


