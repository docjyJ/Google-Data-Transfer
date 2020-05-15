package fr.docjyJ.googleTransfer.app;

import com.google.api.client.util.ArrayMap;
import fr.docjyJ.googleTransfer.api.Utils.TemplateObject;
import fr.docjyJ.googleTransfer.api.Utils.Service;
import freemarker.template.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class Main {
    private final Service serviceOld;
    private final Service serviceNew;
    private final String IconCalendar;
    private final String IconYoutube;
    private final String IconGmail;
    private List<Setting> settings;
    Map<String, Object> newValues;
    Map<String, Object> oldValues;

    //main
    public static void main(String[] args) throws Exception {
        new Main().readActivity();
    }

    //Constructor
    private Main() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.updateComponentTreeUI(new JFrame());
        showWarn(Lang.WARNING);
        this.IconYoutube = createImageByte(ImageIO.read(getClass().getResource("/icon/Youtube.png")));
        this.IconCalendar = createImageByte(ImageIO.read(getClass().getResource("/icon/Calendar.png")));
        this.IconGmail = createImageByte(ImageIO.read(getClass().getResource("/icon/Gmail.png")));
        showInfo(Lang.FIRST_STEP);
        this.serviceOld = new Service();
        showInfo(Lang.SECOND_STEP);
        this.serviceNew = new Service();
    }

    //Activity
    private void readActivity() {
        settings = Arrays.asList(
                new Setting("calendar", Lang.CALENDAR, IconCalendar),
                new Setting("like", Lang.LIKES, IconYoutube),
                new Setting("dislike", Lang.DISLIKES, IconYoutube),
                new Setting("subscription", Lang.SUBSCRIPTIONS, IconYoutube),
                new Setting("playlist", Lang.PLAYLISTS, IconYoutube),
                new Setting("label", Lang.LABELS, IconGmail),
                new Setting("filter", Lang.FILTERS, IconGmail));
        for (Setting setting: settings) {
            switch (setting.name){
                case "calendar" :
                    setting.value = showQuestion(Lang.ASK_CALENDARS);
                    break;
                case "like" :
                    setting.value = showQuestion(Lang.ASK_LIKES);
                    break;
                case "dislike" :
                    setting.value = showQuestion(Lang.ASK_DISLIKES);
                    break;
                case "subscription" :
                    setting.value = showQuestion(Lang.ASK_SUBSCRIPTIONS);
                    break;
                case "playlist" :
                    setting.value = showQuestion(Lang.ASK_PLAYLISTS);
                    break;
                case "label" :
                    setting.value = showQuestion(Lang.ASK_LABELS);
                    break;
                case "filter" :
                    setting.value = showQuestion(Lang.ASK_FILTERS);
                    break;
            }
        }
        newValues = new ArrayMap<>();
        oldValues = new ArrayMap<>();
        for (Setting setting: settings) {
            if(setting.value){
                try{
                    switch (setting.name){
                        case "calendar" :
                            newValues.put("calendar", serviceNew.getCalendar().readCalendars().getCalendars());
                            oldValues.put("calendar", serviceOld.getCalendar().readCalendars().getCalendars());
                            break;
                        case "like" :
                            newValues.put("like", serviceNew.getYoutube().readLike().getLikes());
                            oldValues.put("like", serviceOld.getYoutube().readLike().getLikes());
                            break;
                        case "dislike" :
                            newValues.put("dislike", serviceNew.getYoutube().readDislike().getDislikes());
                            oldValues.put("dislike", serviceOld.getYoutube().readDislike().getDislikes());
                            break;
                        case "subscription" :
                            newValues.put("subscription", serviceNew.getYoutube().readSubscriptions().getSubscriptions());
                            oldValues.put("subscription", serviceOld.getYoutube().readSubscriptions().getSubscriptions());
                            break;
                        case "playlist" :
                            newValues.put("playlist", serviceNew.getYoutube().readPlaylist().getPlaylists());
                            oldValues.put("playlist", serviceOld.getYoutube().readPlaylist().getPlaylists());
                            break;
                        case "label" :
                            newValues.put("label", serviceNew.getGmail().readLabels().getLabels());
                            oldValues.put("label", serviceOld.getGmail().readLabels().getLabels());
                            break;
                        case "filter" :
                            newValues.put("filter", serviceNew.getGmail().readFilters().getFilters());
                            oldValues.put("filter", serviceOld.getGmail().readFilters().getFilters());
                            break;
                    }
                } catch (IOException e) {
                    setting.value = false;
                    showError(e);
                }
            }
        }
        if(showQuestion(Lang.ASK_CHECK)){
            try {
                generatePage();
            } catch (Exception e) {
                showError(e);
            }
        }
        showInfo(Lang.READY);
        putActivity();
    }
    private void putActivity() {
        for (Setting entry: settings) {
            if(entry.value){
                try{
                    switch (entry.name){
                        case "calendar" :
                            newValues.replace("calendar", serviceNew.getCalendar()
                                    .putCalendars(serviceOld.getCalendar().getCalendars())
                                    .readCalendars()
                                    .getCalendars());
                            break;
                        case "like" :
                            newValues.replace("like", serviceNew.getYoutube()
                                    .putLike(serviceOld.getYoutube().getLikes())
                                    .readLike()
                                    .getLikes());
                            break;
                        case "dislike" :
                            newValues.replace("dislike", serviceNew.getYoutube()
                                    .putDislike(serviceOld.getYoutube().getDislikes())
                                    .readDislike()
                                    .getDislikes());
                            break;
                        case "subscription" :
                            newValues.replace("subscription", serviceNew.getYoutube()
                                    .putSubscriptions(serviceOld.getYoutube().getSubscriptions())
                                    .readSubscriptions()
                                    .getSubscriptions());
                            break;
                        case "playlist" :
                            newValues.replace("playlist", serviceNew.getYoutube()
                                    .putPlaylist(serviceOld.getYoutube().getPlaylists())
                                    .readPlaylist()
                                    .getPlaylists());
                            break;
                        case "label" :
                            newValues.replace("label", serviceNew.getGmail()
                                    .putLabels(serviceOld.getGmail().getLabels())
                                    .readLabels()
                                    .getLabels());
                            break;
                        case "filter" :
                            newValues.replace("filter", serviceNew.getGmail()
                                    .putFilters(serviceOld.getGmail().getFilters())
                                    .readFilters()
                                    .getFilters());
                            break;
                    }
                } catch (IOException e) {
                    showError(e);
                }
            }
        }
        if(showQuestion(Lang.ASK_END_CHECK)){
            try {
                generatePage();
            } catch (Exception e) {
                showError(e);
            }
        }
        exit(0);
    }

    //Component
    private void generatePage() throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        newValues.put("ACCOUNT", Collections.singletonList(new TemplateObject<>(
                serviceNew.getUserMail(), serviceNew.getUserName(), createImageByte(serviceNew.getUserPhoto()))));
        oldValues.put("ACCOUNT", Collections.singletonList(new TemplateObject<>(
                serviceOld.getUserMail(), serviceOld.getUserName(), createImageByte(serviceOld.getUserPhoto()))));
        Map<String, Object> input = new ArrayMap<>();
        input.put("title", Lang.APPLICATION_NAME);
        input.put("settings", settings);
        input.put("services", Arrays.asList(oldValues,newValues));
        input.put("style", createStringFile("/style.css"));
        input.put("script", createStringFile("/script.js"));
        input.put("hide", Lang.HIDE_BUTTON);
        input.put("warning", Lang.WARNING);
        File file =new File("Google_Data_Transfer_Generated_" +
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(new Date().getTime()) +
                ".html");
        Template template = cfg.getTemplate("template.ftl");
        template.process(input, new FileWriter(file));
        Desktop.getDesktop().browse(file.toURI());
    }
    private String createStringFile(String path) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(
                new FileInputStream(getClass().getResource(path).getFile())));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while(line != null){
            sb.append(line);
            line = buf.readLine();
        }
        return sb.toString();

    }
    private String createImageByte(Image image) throws IOException {
        BufferedImage bImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos );
        String encodedString = Base64
                .getEncoder()
                .encodeToString(bos.toByteArray());
        return "data:image/png;base64, "+encodedString;
    }


    //Dialog Box
    private void showError(Exception e){
        e.printStackTrace();
        int integr = JOptionPane.showConfirmDialog(null,
                e.getMessage(),
                Lang.APPLICATION_NAME,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE);
        if(integr != 0)
            exit(integr);

    }
    private void showWarn(String message){
        int integer = JOptionPane.showConfirmDialog(null,
                message,
                Lang.APPLICATION_NAME,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if(integer != 0)
            exit(integer);

    }
    private boolean showQuestion(String message){
        int integer = JOptionPane.showConfirmDialog(null,
                message,
                Lang.APPLICATION_NAME,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(integer != 0 & integer != 1)
            exit(0);
        return 0 == integer;
    }
    private void showInfo(String message){
        int integer = JOptionPane.showConfirmDialog(null,
                message,
                Lang.APPLICATION_NAME,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if(integer != 0)
            exit(0);
    }
    private void exit(int status){
        JOptionPane.showMessageDialog(null,
                Lang.EXIT,
                Lang.APPLICATION_NAME,
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(status);

    }


    //Settings
    public static class Setting {
        public String name;
        public boolean value;
        public String lang;
        public String image;

        public Setting(String name, String lang, String image) {
            this.name = name;
            this.lang = lang;
            this.image = image;
            this.value = false;
        }

        public String getName() {
            return name;
        }

        public boolean isValue() {
            return value;
        }

        public String getLang() {
            return lang;
        }

        public String getImage() {
            return image;
        }
    }
}
