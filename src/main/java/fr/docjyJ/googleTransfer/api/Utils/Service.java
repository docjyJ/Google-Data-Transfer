package fr.docjyJ.googleTransfer.api.Utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.drive.Drive;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.youtube.YouTube;
import com.google.gdata.client.contacts.ContactsService;
import fr.docjyJ.googleTransfer.api.Services.calendar.CalendarElement;
import fr.docjyJ.googleTransfer.api.Services.contact.ContactElement;
import fr.docjyJ.googleTransfer.api.Services.drive.DriveElement;
import fr.docjyJ.googleTransfer.api.Services.gmail.GmailElement;
import fr.docjyJ.googleTransfer.api.Services.youtube.YoutubeElement;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Service extends GoogleTransfer {
    protected static final String CLIENT_SECRETS = "client_secret.json";
    protected static final String APPLICATION_NAME = "Google Transfer";
    protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    protected static final Collection<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/youtube",
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/photoslibrary",
            "https://www.googleapis.com/auth/gmail.modify",
            "https://www.google.com/m8/feeds/");

    //ELEMENT
    protected transient Credential credential;
    protected ContactElement contacts;
    protected YoutubeElement youtube;
    protected CalendarElement calendar;
    protected GmailElement gmail;
    protected DriveElement drive;

    //CONSTRUCTOR
    public Service() throws Exception {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.credential = new AuthorizationCodeInstalledApp(
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport,
                        JSON_FACTORY,
                        GoogleClientSecrets.load(
                                JSON_FACTORY,
                                new InputStreamReader(
                                Objects.requireNonNull(
                                        Service.class.getClassLoader().getResourceAsStream(CLIENT_SECRETS)))),
                        SCOPES).build(),
                new LocalServerReceiver()).authorize("user");

        //Contacts
        this.contacts = new ContactElement(new ContactsService(APPLICATION_NAME));
        this.contacts.getService().setOAuth2Credentials(this.credential);

        //Youtube
        this.youtube = new YoutubeElement(new YouTube.Builder(httpTransport, JSON_FACTORY, this.credential)
                .setApplicationName(APPLICATION_NAME)
                .build());

        //Calendar
        this.calendar = new CalendarElement(new Calendar.Builder(httpTransport, JSON_FACTORY, this.credential)
                .setApplicationName(APPLICATION_NAME)
                .build());

        //Gmail
        this.gmail = new GmailElement(new Gmail.Builder(httpTransport, JSON_FACTORY, this.credential)
                .setApplicationName(APPLICATION_NAME)
                .build());

        //Drive
        this.drive = new DriveElement(new Drive.Builder(httpTransport, JSON_FACTORY, this.credential)
                .setApplicationName(APPLICATION_NAME)
                .build());
    }

    //READ
    public Service readAll() throws Exception {
        this.calendar = this.calendar.readAll();
        this.youtube = this.youtube.readAll();
        this.contacts = this.contacts.readAll();
        this.gmail = this.gmail.readAll();
        this.drive = this.drive.readAll();
        return this;
    }

    //PUT
    public Service putAll(Service data) throws Exception {
        this.contacts.putAll(data.contacts);
        this.youtube.putAll(data.youtube);
        this.calendar.putAll(data.calendar);
        this.gmail.putAll(data.gmail);
        this.drive.putAll(data.drive);
        return this;
    }

    //GET
    public Credential getCredential() {
        return credential;
    }
    public ContactElement getContacts() {
        return contacts;
    }
    public YoutubeElement getYoutube() {
        return youtube;
    }
    public CalendarElement getCalendar() {
        return calendar;
    }
    public GmailElement getGmail() {
        return gmail;
    }
    public DriveElement getDrive() {
        return drive;
    }
    public ContactsService getContactsService() {
        return contacts.getService();
    }
    public YouTube getYoutubeService() {
        return youtube.getService();
    }
    public Calendar getCalendarService() {
        return calendar.getService();
    }
    public Gmail getGmailService() {
        return gmail.getService();
    }
    public Drive getDriveService() {
        return drive.getService();
    }

}