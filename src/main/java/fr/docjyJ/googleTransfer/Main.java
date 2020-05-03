package fr.docjyJ.googleTransfer;

import fr.docjyJ.googleTransfer.Utils.Service;

public class Main {
    public static void main(String[] args) throws Exception {
        new Service().getCalendar()
                .readAll()
                .putAll(new Service().getCalendarService())
                .print();

        /* ====Sample test==== *
        Test all services with all parties
        new Service()
                .readAll()
                .putAll(new Service())
                .print();

        Test a single service with all its parts
        new Service().getGmail()
                .readAll()
                .putAll(new Service().getGmailService())
                .print();

        Test part of a service
        new Service().getYoutube()
                .readPlaylist()
                .putPlaylist(new Service().getYoutubeService())
                .print();
         */

    }
}


