package fr.docjyJ.googleTransfer.Utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.youtube.YouTube;
import com.google.gdata.client.contacts.ContactsService;
import fr.docjyJ.googleTransfer.Services.calendar.CalendarElement;
import fr.docjyJ.googleTransfer.Services.contact.ContactElement;
import fr.docjyJ.googleTransfer.Services.youtube.YoutubeElement;

import java.io.InputStreamReader;
import java.util.Objects;

public class Service extends GoogleTransfer {
    //ELEMENT
    transient Credential credential;
    ContactElement contacts;
    YoutubeElement youtube;
    CalendarElement calendar;

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
    }

    //READ
    public Service readAll() throws Exception {
        this.calendar = this.calendar.readAll();
        this.youtube = this.youtube.readAll();
        this.contacts = this.contacts.readAll();
        return this;
    }

    //PUT
    public Service putAll(Service newClient) throws Exception {
        contacts.putAll(newClient.getContactService());
        youtube.putAll(newClient.getYoutubeService());
        calendar.putAll(newClient.getCalendarService());
        return this;
    }

    //GET
    public ContactsService getContactService(){
        return contacts.getService();
    }
    public Calendar getCalendarService(){
        return calendar.getService();
    }
    public YouTube getYoutubeService(){
        return youtube.getService();
    }
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
}
