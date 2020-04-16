package fr.docjyJ.googleTransfer;

import com.google.api.services.youtube.YouTube;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import static fr.docjyJ.googleTransfer.api.GoogleOauth.*;
import static fr.docjyJ.googleTransfer.api.YouTubeData.*;

public class Main {
    private static final Scanner scan = new Scanner(System.in);

    public static boolean question(String str){
        String answer;
        System.out.println(str);
        while (true) {
            answer = scan.nextLine().trim().toLowerCase();
            if (answer.equals(Lang.YES)) {
                return true;
            } else if (answer.equals(Lang.NO)) {
                return false;
            } else {
                System.out.println(Lang.BAD_ANSWER);
            }
        }
    }

    public static void main(String[] args)
            throws GeneralSecurityException, IOException, InterruptedException {
        YouTube compteA = getService(Lang.FIRST_STEP);
        YouTube compteB = getService(Lang.SECOND_STEP);

        if(question(Lang.ASK_LIKED))
            transferLike(compteA,compteB,"","like");
        if(question(Lang.ASK_DISLIKED))
            transferLike(compteA,compteB,"","dislike");


    }
}


