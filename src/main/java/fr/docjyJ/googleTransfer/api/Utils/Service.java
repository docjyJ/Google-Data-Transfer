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
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.youtube.YouTube;
import fr.docjyJ.googleTransfer.api.Services.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Service extends GoogleTransfer<Credential> {
    transient protected static final String APPLICATION_NAME = "Google Transfer";
    transient protected static final Collection<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/youtube",
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/photoslibrary",
            "https://www.googleapis.com/auth/gmail.modify",
            "https://www.googleapis.com/auth/contacts");

    //ELEMENT
    protected UserInfo user;
    protected ContactElement contacts;
    protected YoutubeElement youtube;
    protected CalendarElement calendar;
    protected GmailElement gmail;
    protected DriveElement drive;

    //CONSTRUCTOR
    public Service() throws Exception {
        super();
        final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        final NetHttpTransport netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.service =new AuthorizationCodeInstalledApp(
            new GoogleAuthorizationCodeFlow.Builder(
                netHttpTransport,
                jsonFactory,
                GoogleClientSecrets.load(
                    jsonFactory,
                    new InputStreamReader(
                        Objects.requireNonNull(Service.class.getClassLoader().getResourceAsStream("client_secret.json")))),
                SCOPES).build(),
            new LocalServerReceiver()).authorize("user");

        //Contacts
        this.contacts = new ContactElement(new PeopleService.Builder(netHttpTransport, jsonFactory, service)
                .setApplicationName(APPLICATION_NAME)
                .build());

        //Youtube
        this.youtube = new YoutubeElement(new YouTube.Builder(netHttpTransport, jsonFactory, service)
                .setApplicationName(APPLICATION_NAME)
                .build());

        //Calendar
        this.calendar = new CalendarElement(new Calendar.Builder(netHttpTransport, jsonFactory, service)
                .setApplicationName(APPLICATION_NAME)
                .build());

        //Gmail
        this.gmail = new GmailElement(new Gmail.Builder(netHttpTransport, jsonFactory, service)
                .setApplicationName(APPLICATION_NAME)
                .build());

        //Drive
        this.drive = new DriveElement(new Drive.Builder(netHttpTransport, jsonFactory, service)
                .setApplicationName(APPLICATION_NAME)
                .build());

        //userInfo
        this.user = new UserInfo(contacts);
    }

    //READ
    public Service readAll() throws IOException {
        this.calendar = this.calendar.readAll();
        this.youtube = this.youtube.readAll();
        this.contacts = this.contacts.readAll();
        this.gmail = this.gmail.readAll();
        this.drive = this.drive.readAll();
        return this;
    }

    //PUT
    public Service putAll(Service data) throws IOException {
        this.contacts.putAll(data.contacts);
        this.youtube.putAll(data.youtube);
        this.calendar.putAll(data.calendar);
        this.gmail.putAll(data.gmail);
        this.drive.putAll(data.drive);
        return this;
    }

    //GET
    public UserInfo getUser() {
        return user;
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

    //OBJECT
    public static class UserInfo{
        protected transient Image photo;
        protected String mail;
        protected String name;

        public UserInfo(ContactElement contacts) throws IOException {
            Person userInfo = contacts.getService().people()
                    .get("people/me")
                    .setPersonFields("photos,emailAddresses,names")
                    .execute();
            this.mail = userInfo.getEmailAddresses().get(0).getValue();
            this.name = userInfo.getNames().get(0).getDisplayName();
            this.photo = new ImageIcon(ImageIO.read(new URL(userInfo.getPhotos().get(0).getUrl()))).getImage();
        }

        public Image getPhoto() {
            return photo;
        }

        public String getMail() {
            return mail;
        }

        public String getName() {
            return name;
        }
    }

}
