package fr.docjyJ.googleTransfer.api;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.calendar.Calendar;
import com.google.gdata.client.contacts.ContactsService;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import static fr.docjyJ.googleTransfer.Lang.systemLog;

public class GoogleOauth {
    private static final String CLIENT_SECRETS= "client_secret.json";
    private static final Collection<String> SCOPES = List.of(
            "https://www.googleapis.com/auth/youtube",
            "https://www.googleapis.com/auth/calendar",
            /*"https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/photoslibrary",
            "https://www.googleapis.com/auth/gmail.settings.basic",
            "https://www.googleapis.com/auth/fitness.activity.write",
            "https://www.googleapis.com/auth/fitness.blood_glucose.write",
            "https://www.googleapis.com/auth/fitness.blood_pressure.write",
            "https://www.googleapis.com/auth/fitness.body.write",
            "https://www.googleapis.com/auth/fitness.body_temperature.write",
            "https://www.googleapis.com/auth/fitness.location.write",
            "https://www.googleapis.com/auth/fitness.nutrition.write",
            "https://www.googleapis.com/auth/fitness.oxygen_saturation.write",
            "https://www.googleapis.com/auth/fitness.reproductive_health.write",*/
            "https://www.google.com/m8/feeds/");

    private static final String APPLICATION_NAME = "Google Transfer";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static class Service{
        ContactsService contactsService;
        YouTube youtubeService;
        Calendar calendarService;

        public Service(ContactsService contactsService, YouTube youtubeService, Calendar calendarService) {
            this.contactsService = contactsService;
            this.youtubeService = youtubeService;
            this.calendarService = calendarService;
        }

        public ContactsService getContactsService() {
            return contactsService;
        }

        public YouTube getYoutubeService() {
            return youtubeService;
        }

        public Calendar getCalendarService() {
            return calendarService;
        }
    }

    public static Credential authorize(final NetHttpTransport httpTransport) throws Exception {
        // Load client secrets.
        InputStream in = GoogleOauth.class
                .getClassLoader().getResourceAsStream(CLIENT_SECRETS);
        assert in != null;
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static Service getService(String info) throws Exception {
        systemLog(info);
        Thread.sleep(5000);
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);

        //Contacts
        ContactsService contactsService = new ContactsService(APPLICATION_NAME);
        contactsService.setOAuth2Credentials(credential);

        return new Service(contactsService,

                //Youtube
                new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build(),

                //Calendar
                new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build());
    }

}
