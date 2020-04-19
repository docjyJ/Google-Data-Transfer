package fr.docjyJ.googleTransfer;

import java.util.Scanner;

public class Lang {
    public static final String YES = "y";
    public static final String NO = "n";
    public static final String YES_NO = "(" + YES + "/" + NO + ")";
    public static final String FIRST_STEP = "First we will connect to the account that contains the data.";
    public static final String SECOND_STEP = "Then we will connect to the account that will receive the data.";
    public static final String ASK_LIKED = "Do you want to transfer liked videos? " + YES_NO;
    public static final String ASK_DISLIKED = "Do you want to transfer disliked videos? " + YES_NO;
    public static final String ASK_SUBSCRIPTION = "Do you want to transfer subscribed channels?" + YES_NO;
    public static final String ASK_PLAYLIST = "Do you want to transfer playlists?" + YES_NO;
    public static final String ASK_ERROR = "Do you want to continue? " + YES_NO;
    public static final String BAD_ANSWER ="Sorry, I didn't catch that. Please answer " + YES + "/" + NO;
    public static final String PREFIX_INFO ="[INFO]: ";
    public static final String PREFIX_ERROR ="[ERROR]: ";

    private static final Scanner scan = new Scanner(System.in);

    public static boolean question(String code){
        String answer;
        switch (code) {
            case "error":
                systemLog(ASK_ERROR);
                break;
            case "like":
                systemLog(ASK_LIKED);
                break;
            case "dislike":
                systemLog(ASK_DISLIKED);
                break;
            case "subscriptions":
                systemLog(ASK_SUBSCRIPTION);
                break;
            case "playlist":
                systemLog(ASK_PLAYLIST);
                break;
        }

        while (true) {
            answer = scan.nextLine().trim().toLowerCase();
            if (answer.equals(YES)) {
                return true;
            } else if (answer.equals(NO)) {
                return false;
            } else {
                System.out.println(BAD_ANSWER);
            }
        }
    }
    public static void systemLog(Object message){
        System.out.println(PREFIX_INFO + message.toString());
    }
    public static void systemLogError(Object message){
        System.out.println(PREFIX_ERROR + message.toString());
        if(!question("error"))
            System.exit(1);
    }
}
