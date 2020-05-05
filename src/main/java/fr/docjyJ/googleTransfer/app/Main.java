package fr.docjyJ.googleTransfer.app;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.Subscription;
import fr.docjyJ.googleTransfer.api.Services.calendar.CalendarItemElement;
import fr.docjyJ.googleTransfer.api.Services.youtube.PlaylistElement;
import fr.docjyJ.googleTransfer.api.Utils.Service;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Main extends JFrame {
    private final Service serviceOld;
    private final Service serviceNew;
    private List<Setting> settings;

    //main/start
    public static void main(String[] args) throws Exception {
        new Main().start();
    }
    public void start(){
        readActivity();
    }

    //Constructor
    Main() throws Exception {
        consoleView();
        setTitle(Lang.APPLICATION_NAME);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

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
    }

    //Activity
    private void readActivity(){
        consoleView();
        settings = Arrays.asList(
                new Setting("calendar"),
                new Setting("like"),
                new Setting("dislike"),
                new Setting("subscription"),
                new Setting("playlist"));
        for (Setting setting: settings) {
            switch (setting.name){
                case "calendar" :
                    setting.value = showQuestion(Lang.ASK_CALENDARS);
                    break;
                case "like" :
                    setting.value = showQuestion(Lang.ASK_LIKE);
                    break;
                case "dislike" :
                    setting.value = showQuestion(Lang.ASK_DISLIKE);
                    break;
                case "subscription" :
                    setting.value = showQuestion(Lang.ASK_SUBSCRIPTION);
                    break;
                case "playlist" :
                    setting.value = showQuestion(Lang.ASK_PLAYLIST);
                    break;
            }
        }
        for (Setting setting: settings) {
            if(setting.value){
                try{
                    switch (setting.name){
                        case "calendar" :
                            serviceNew.getCalendar().readCalendars();
                            serviceOld.getCalendar().readCalendars();
                            break;
                        case "like" :
                            serviceNew.getYoutube().readLike();
                            serviceOld.getYoutube().readLike();
                            break;
                        case "dislike" :
                            serviceNew.getYoutube().readDislike();
                            serviceOld.getYoutube().readDislike();
                            break;
                        case "subscription" :
                            serviceNew.getYoutube().readSubscriptions();
                            serviceOld.getYoutube().readSubscriptions();
                            break;
                        case "playlist" :
                            serviceNew.getYoutube().readPlaylist();
                            serviceOld.getYoutube().readPlaylist();
                            break;
                    }
                } catch (IOException e) {
                    setting.value = false;
                    showError(e);
                }

            }

        }
        treeView(arg0 -> putActivity());
    }
    private void putActivity(){
        consoleView();
        for (Setting entry: settings) {
            if(entry.value){
                try{
                    switch (entry.name){
                        case "calendar" :
                            serviceNew.getCalendar()
                                    .putCalendars(serviceOld.getCalendar().getCalendars())
                                    .readCalendars();
                            break;
                        case "like" :
                            serviceNew.getYoutube()
                                    .putLike(serviceOld.getYoutube().getLikes())
                                    .readLike();
                            break;
                        case "dislike" :
                            serviceNew.getYoutube()
                                    .putDislike(serviceOld.getYoutube().getDislikes())
                                    .readDislike();
                            break;
                        case "subscription" :
                            serviceNew.getYoutube()
                                    .putSubscriptions(serviceOld.getYoutube().getSubscriptions())
                                    .readSubscriptions();
                            break;
                        case "playlist" :
                            serviceNew.getYoutube()
                                    .putPlaylist(serviceOld.getYoutube().getPlaylists())
                                    .readPlaylist();
                            break;
                    }
                } catch (IOException e) {
                    showError(e);
                }

            }

        }
        treeView(arg1 -> consoleView());
    }

    //View
    private void consoleView() {
        JTextArea textArea = new JTextArea(50, 10);
        PrintStream printStream = new PrintStream(new Console(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
        setView(textArea, null);
    }
    private void treeView(ActionListener btnAction) {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeComponent(serviceOld), treeComponent(serviceNew));
        JButton okButton = new JButton(Lang.NEXT);
        okButton.addActionListener(btnAction);
        setView(split, okButton);
    }
    private void setView(Component center, Component south){
        setVisible(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        if(center != null) mainPanel.add(center, BorderLayout.CENTER);
        if(south != null) mainPanel.add(south, BorderLayout.SOUTH);
        this.setContentPane(new JScrollPane(mainPanel));
        setVisible(true);
    }

    //Component
    private Component treeComponent(Service service) {
        Map<String, Boolean> settings = new HashMap<>();
        for (Setting setting : this.settings)
            settings.put(setting.name, setting.value);
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode(Lang.APPLICATION_NAME);

        //Calendar
        if (settings.get("calendar")){
            DefaultMutableTreeNode calendars = new DefaultMutableTreeNode(Lang.CALENDAR);
            for (CalendarItemElement calendarEntry : service.getCalendar().getCalendars()) {
                DefaultMutableTreeNode calendar = new DefaultMutableTreeNode(calendarEntry.getCalendar().getSummary());
                for (Event eventEntry : calendarEntry.getEvents()) {
                    DefaultMutableTreeNode event = new DefaultMutableTreeNode(eventEntry.getSummary());
                    calendar.add(event);
                }
                calendars.add(calendar);
            }
            tree.add(calendars);
        }

        //Youtube
        if (settings.get("like")||settings.get("dislike")||settings.get("subscription")||settings.get("playlist")) {
            DefaultMutableTreeNode youtube = new DefaultMutableTreeNode(Lang.YOUTUBE);
            //like
            if (settings.get("like")) {
                DefaultMutableTreeNode likes = new DefaultMutableTreeNode(Lang.LIKES);
                for (String likeEntry : service.getYoutube().getLikes()) {
                    DefaultMutableTreeNode like = new DefaultMutableTreeNode(likeEntry);
                    likes.add(like);
                }
                youtube.add(likes);
            }
            //dislike
            if (settings.get("dislike")) {
                DefaultMutableTreeNode dislikes = new DefaultMutableTreeNode(Lang.DISLIKES);
                for (String dislikeEntry : service.getYoutube().getDislikes()) {
                    DefaultMutableTreeNode dislike = new DefaultMutableTreeNode(dislikeEntry);
                    dislikes.add(dislike);
                }
                youtube.add(dislikes);
            }
            //subscription
            if (settings.get("subscription")) {
                DefaultMutableTreeNode subscriptions = new DefaultMutableTreeNode(Lang.SUBSCRIPTIONS);
                for (Subscription subscriptionEntry : service.getYoutube().getSubscriptions()) {
                    DefaultMutableTreeNode subscription = new DefaultMutableTreeNode(subscriptionEntry.getSnippet().getResourceId().getChannelId());
                    subscriptions.add(subscription);
                }
                youtube.add(subscriptions);
            }
            //playlist
            if (settings.get("playlist")) {
                DefaultMutableTreeNode playlists = new DefaultMutableTreeNode(Lang.PLAYLISTS);
                for (PlaylistElement playlistEntry : service.getYoutube().getPlaylists()) {
                    DefaultMutableTreeNode playlist = new DefaultMutableTreeNode(playlistEntry.getPlaylist().getSnippet().getTitle());
                    for (PlaylistItem itemEntry : playlistEntry.getItems()) {
                        DefaultMutableTreeNode item = new DefaultMutableTreeNode(itemEntry.getSnippet().getResourceId().getVideoId());
                        playlist.add(item);
                    }
                    playlists.add(playlist);
                }
                youtube.add(playlists);
            }
            tree.add(youtube);
        }

        //Panel
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BorderLayout());
        accountPanel.add(
                new JLabel(new ImageIcon(service.getUserPhoto())),
                BorderLayout.WEST);
        accountPanel.add(
                new JLabel(
                        "<html><font size=\"+2\">"+service.getUserName()+"</font><br/><i>"+service.getUserMail()+"</i></html>",
                        SwingConstants.CENTER),
                BorderLayout.CENTER);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(accountPanel, BorderLayout.NORTH);
        mainPanel.add(new JTree(tree), BorderLayout.CENTER);
        return mainPanel;
    }

    //Dialog Box
    private void showError(Exception e){
        JOptionPane.showMessageDialog(null,
                e.getMessage(),
                Lang.APPLICATION_NAME,
                JOptionPane.ERROR_MESSAGE);

    }
    private boolean showQuestion(String message){
        return 0 == JOptionPane.showConfirmDialog(null,
                message,
                Lang.APPLICATION_NAME,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

    }

    //Settings
    private static class Setting {
        public String name;
        public boolean value;
        public Setting(String type) {
            this.name = type;
            this.value = false;
        }
    }





}
