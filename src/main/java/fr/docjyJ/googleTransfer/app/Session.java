package fr.docjyJ.googleTransfer.app;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.gmail.model.Filter;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.Subscription;
import com.google.gdata.data.contacts.ContactEntry;
import fr.docjyJ.googleTransfer.api.Services.calendar.CalendarItemElement;
import fr.docjyJ.googleTransfer.api.Services.youtube.PlaylistElement;
import fr.docjyJ.googleTransfer.api.Utils.Service;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class Session extends Container {
    private JPanel mainPanel;
    private JTree tree;
    private Service service;

    Session() throws Exception {
        service = new Service();
        setTreeView();
    }

    JPanel setTreeView() throws Exception {
        DefaultMutableTreeNode racine = new DefaultMutableTreeNode("DataTransfer");

        //Calendar
        DefaultMutableTreeNode calendars = new DefaultMutableTreeNode("Google Calendar");
        for (CalendarItemElement calendarEntry :service.getCalendar().readAll().getCalendars()) {
            DefaultMutableTreeNode calendar = new DefaultMutableTreeNode(calendarEntry.getCalendar().getSummary());
            for (Event eventEntry: calendarEntry.getEvents()) {
                DefaultMutableTreeNode event = new DefaultMutableTreeNode(eventEntry.getSummary());
                calendar.add(event);
            }
            calendars.add(calendar);
        }
        racine.add(calendars);

        //Contacts
        DefaultMutableTreeNode contacts = new DefaultMutableTreeNode("Contacts");
        for (ContactEntry contactEntry :service.getContacts().readContacts().getContacts()) {
            DefaultMutableTreeNode contact = new DefaultMutableTreeNode(contactEntry.toString());
            contacts.add(contact);
        }
        racine.add(contacts);

        //Gmail
        DefaultMutableTreeNode gmail = new DefaultMutableTreeNode("Gmail");

        DefaultMutableTreeNode labels = new DefaultMutableTreeNode("Labels");
        for (Label labelEntry :service.getGmail().readLabels().getLabels()) {
            DefaultMutableTreeNode label = new DefaultMutableTreeNode(labelEntry.getName());
            labels.add(label);
        }
        gmail.add(labels);

        DefaultMutableTreeNode filters = new DefaultMutableTreeNode("Filters");
        for (Filter filterEntry :service.getGmail().readFilters().getFilters()) {
            DefaultMutableTreeNode filter = new DefaultMutableTreeNode(filterEntry.getAction().toString());
            filters.add(filter);
        }
        gmail.add(filters);

        racine.add(gmail);

        //Youtube
        DefaultMutableTreeNode youtube = new DefaultMutableTreeNode("Youtube");

        DefaultMutableTreeNode likes = new DefaultMutableTreeNode("Likes");
        for (String likeEntry :service.getYoutube().readLike().getLikes()) {
            DefaultMutableTreeNode like = new DefaultMutableTreeNode(likeEntry);
            likes.add(like);
        }
        youtube.add(likes);

        DefaultMutableTreeNode dislikes = new DefaultMutableTreeNode("Dislikes");
        for (String dislikeEntry :service.getYoutube().readDislike().getDislikes()) {
            DefaultMutableTreeNode dislike = new DefaultMutableTreeNode(dislikeEntry);
            dislikes.add(dislike);
        }
        youtube.add(dislikes);

        DefaultMutableTreeNode subscriptions = new DefaultMutableTreeNode("Dislikes");
        for (Subscription subscriptionEntry :service.getYoutube().readSubscriptions().getSubscriptions()) {
            DefaultMutableTreeNode subscription = new DefaultMutableTreeNode(subscriptionEntry.getSnippet().getResourceId().getChannelId());
            subscriptions.add(subscription);
        }
        youtube.add(subscriptions);

        DefaultMutableTreeNode playlists = new DefaultMutableTreeNode("Playlists");
        for (PlaylistElement playlistEntry :service.getYoutube().readPlaylist().getPlaylists()) {
            DefaultMutableTreeNode playlist = new DefaultMutableTreeNode(playlistEntry.getPlaylist().getSnippet().getTitle());
            for (PlaylistItem itemEntry: playlistEntry.getItems()) {
                DefaultMutableTreeNode item = new DefaultMutableTreeNode(itemEntry.getSnippet().getResourceId().getVideoId());
                playlist.add(item);
            }
            playlists.add(playlist);
        }
        youtube.add(playlists);

        racine.add(youtube);



        tree = new JTree(racine);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 1));
        mainPanel.add(tree);
        return mainPanel;
    }
}
