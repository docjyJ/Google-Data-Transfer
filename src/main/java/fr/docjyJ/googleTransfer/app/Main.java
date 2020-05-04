package fr.docjyJ.googleTransfer.app;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.Subscription;
import fr.docjyJ.googleTransfer.api.Services.calendar.CalendarElement;
import fr.docjyJ.googleTransfer.api.Services.calendar.CalendarItemElement;
import fr.docjyJ.googleTransfer.api.Services.youtube.PlaylistElement;
import fr.docjyJ.googleTransfer.api.Services.youtube.YoutubeElement;
import fr.docjyJ.googleTransfer.api.Utils.Service;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;

class Main extends JFrame {
    private Service serviceOld;
    private Service serviceNew;

    public static void main(String[] args) throws Exception {
        new Main();
    }

    //Constructor
    Main() throws Exception {
        setTitle(Lang.APPLICATION_NAME);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setConsole();

        JOptionPane.showMessageDialog(null,
                Lang.FIRST_STEP,
                Lang.APPLICATION_NAME,
                JOptionPane.INFORMATION_MESSAGE);
        this.serviceOld = new Service();
        JOptionPane.showMessageDialog(null,
                Lang.SECOND_STEP,
                Lang.APPLICATION_NAME,
                JOptionPane.INFORMATION_MESSAGE);
        this.serviceNew = new Service();
        setNext("first");
    }

    //Setters
    private void setNext(String now) {
        setConsole();
        if (now.equals("first"))
            try{
                setYoutube();
            } catch (IOException e) {
                showError(e);
                setNext("youtube");
            }
        if (now.equals("youtube"))
            try{
                setCalendar();
            } catch (IOException e) {
                showError(e);
                setNext("calendar");
            }
    }
    private void setMainPanel(Component center, Component south){
        setVisible(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        if(center != null) mainPanel.add(center, BorderLayout.CENTER);
        if(south != null) mainPanel.add(south, BorderLayout.SOUTH);
        this.setContentPane(new JScrollPane(mainPanel));
        setVisible(true);
    }
    private void setConsole() {
        JTextArea textArea = new JTextArea(50, 10);
        PrintStream printStream = new PrintStream(new Console(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
        setMainPanel(textArea, null);
    }
    private void setTreeView(JTree treeOld, JTree treeNew,ActionListener btnAction) {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeOld, treeNew);
        JButton okButton = new JButton(Lang.NEXT);
        okButton.addActionListener(btnAction);
        setMainPanel(split, okButton);
    }
    private void showError(Exception e){
        JOptionPane.showMessageDialog(null,
                e.getMessage(),
                Lang.APPLICATION_NAME,
                JOptionPane.ERROR_MESSAGE);

    }
    private int showQuestion(String message){
        return JOptionPane.showConfirmDialog(null,
                message,
                Lang.APPLICATION_NAME,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

    }


    //Calendar
    private void setCalendar() throws IOException {
        if (showQuestion(Lang.ASK_CALENDAR) != 0) {
            throw new IOException("Refused");
        }
        serviceOld.getCalendar().readAll();
        serviceNew.getCalendar().readAll();
        setTreeView(
                setTreeCalendar(serviceOld.getCalendar()),
                setTreeCalendar(serviceNew.getCalendar()),
                arg0 -> {
                    int calendar = showQuestion(Lang.ASK_CALENDARS);
                    setConsole();
                    if(calendar == 0) {
                        try {
                            serviceNew.getCalendar()
                                    .putCalendars(serviceOld.getCalendar().getCalendars())
                                    .readCalendars();
                        } catch (Exception e) {
                            showError(e);
                        }
                    }
                    setTreeView(
                            setTreeCalendar(serviceOld.getCalendar()),
                            setTreeCalendar(serviceNew.getCalendar()),
                            arg1 -> setNext("calendar"));

                });
    }
    private JTree setTreeCalendar(CalendarElement calendarsEntry) {
        DefaultMutableTreeNode calendars = new DefaultMutableTreeNode(Lang.CALENDAR);
        for (CalendarItemElement calendarEntry :calendarsEntry.getCalendars()) {
            DefaultMutableTreeNode calendar = new DefaultMutableTreeNode(calendarEntry.getCalendar().getSummary());
            for (Event eventEntry: calendarEntry.getEvents()) {
                DefaultMutableTreeNode event = new DefaultMutableTreeNode(eventEntry.getSummary());
                calendar.add(event);
            }
            calendars.add(calendar);
        }
        return new JTree(calendars);
    }

    //YouTube
    private void setYoutube() throws IOException {
        if (showQuestion(Lang.ASK_YOUTUBE) != 0) {
            throw new IOException("Refused");
        }
        serviceNew.getYoutube().readAll();
        serviceOld.getYoutube().readAll();
        setTreeView(
                setTreeYoutube(serviceOld.getYoutube()),
                setTreeYoutube(serviceNew.getYoutube()),
                arg0 -> {
                    int like = showQuestion(Lang.ASK_LIKE);
                    int dislike = showQuestion(Lang.ASK_DISLIKE);
                    int subscription = showQuestion(Lang.ASK_SUBSCRIPTION);
                    int playlist = showQuestion(Lang.ASK_PLAYLIST);
                    setConsole();
                    if(like == 0) {
                        try {
                            serviceNew.getYoutube()
                                    .putLike(serviceOld.getYoutube().getLikes())
                                    .readLike();
                        } catch (IOException e) {
                            showError(e);
                        }
                    }
                    if(dislike == 0) {
                        try {
                            serviceNew.getYoutube()
                                    .putDislike(serviceOld.getYoutube().getDislikes())
                                    .readDislike();
                        } catch (IOException e) {
                            showError(e);
                        }
                    }
                    if(subscription == 0) {
                        try {
                            serviceNew.getYoutube()
                                    .putSubscriptions(serviceOld.getYoutube().getSubscriptions())
                                    .readSubscriptions();
                        } catch (IOException e) {
                            showError(e);
                        }
                    }
                    if(playlist == 0) {
                        try {
                            serviceNew.getYoutube()
                                    .putPlaylist(serviceOld.getYoutube().getPlaylists())
                                    .readPlaylist();
                        } catch (IOException e) {
                            showError(e);
                        }
                    }
                    setTreeView(
                            setTreeYoutube(serviceOld.getYoutube()),
                            setTreeYoutube(serviceNew.getYoutube()),
                            arg1 -> setNext("youtube"));

                });
    }
    private JTree setTreeYoutube(YoutubeElement youtubeEntry) {
        DefaultMutableTreeNode youtube = new DefaultMutableTreeNode(Lang.YOUTUBE);
        //like
        DefaultMutableTreeNode likes = new DefaultMutableTreeNode(Lang.LIKES);
        for (String likeEntry : youtubeEntry.getLikes()) {
            DefaultMutableTreeNode like = new DefaultMutableTreeNode(likeEntry);
            likes.add(like);
        }
        youtube.add(likes);
        //dislike
        DefaultMutableTreeNode dislikes = new DefaultMutableTreeNode(Lang.DISLIKES);
        for (String dislikeEntry : youtubeEntry.getDislikes()) {
            DefaultMutableTreeNode dislike = new DefaultMutableTreeNode(dislikeEntry);
            dislikes.add(dislike);
        }
        youtube.add(dislikes);
        DefaultMutableTreeNode subscriptions = new DefaultMutableTreeNode(Lang.SUBSCRIPTIONS);
        for (Subscription subscriptionEntry : youtubeEntry.getSubscriptions()) {
            DefaultMutableTreeNode subscription = new DefaultMutableTreeNode(subscriptionEntry.getSnippet().getResourceId().getChannelId());
            subscriptions.add(subscription);
        }
        youtube.add(subscriptions);
        DefaultMutableTreeNode playlists = new DefaultMutableTreeNode(Lang.PLAYLISTS);
        for (PlaylistElement playlistEntry : youtubeEntry.getPlaylists()) {
            DefaultMutableTreeNode playlist = new DefaultMutableTreeNode(playlistEntry.getPlaylist().getSnippet().getTitle());
            for (PlaylistItem itemEntry: playlistEntry.getItems()) {
                DefaultMutableTreeNode item = new DefaultMutableTreeNode(itemEntry.getSnippet().getResourceId().getVideoId());
                playlist.add(item);
            }
            playlists.add(playlist);
        }
        youtube.add(playlists);

        return new JTree(youtube);
    }

}
