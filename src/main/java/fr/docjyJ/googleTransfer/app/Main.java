package fr.docjyJ.googleTransfer.app;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.Subscription;
import fr.docjyJ.googleTransfer.api.Services.calendar.CalendarItemElement;
import fr.docjyJ.googleTransfer.api.Services.youtube.PlaylistElement;
import fr.docjyJ.googleTransfer.api.Utils.Service;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

class Main {
    private final Service serviceOld;
    private final Service serviceNew;
    private List<Setting> settings;

    Document document;

    //main/start
    public static void main(String[] args) throws Exception {
        new Main().readActivity();
    }

    //Constructor
    private Main() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.updateComponentTreeUI(new JFrame());
        showInfo(Lang.FIRST_STEP);
        this.serviceOld = new Service();
        showInfo(Lang.SECOND_STEP);
        this.serviceNew = new Service();
    }

    //Activity
    private void readActivity() {
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
    private void createRecourse(String resourcePath, String contentPath) throws Exception {
        InputStream stream = Main.class.getResourceAsStream("/"+resourcePath);
        if(stream == null) {
            throw new IOException("Cannot get resource \"" + resourcePath + "\" from Jar file.");
        }

        int readBytes;
        byte[] buffer = new byte[4096];
        OutputStream resStreamOut = new FileOutputStream(contentPath);
        while ((readBytes = stream.read(buffer)) > 0) {
            resStreamOut.write(buffer, 0, readBytes);
        }
        stream.close();
        stream.close();
    }
    private void generatePage() throws Exception {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element body = document.createElement("body");
        body.appendChild(generateAccount(serviceOld));
        body.appendChild(generateAccount(serviceNew));

        Element html = document.createElement("html");
        html.appendChild(generateHeader());
        html.appendChild(body);
        html.setAttribute("lang","en");
        document.appendChild(html);

        String path = "GoogleDataTransferTemp"+new Date().getTime()+"/";
        File htmlFile = new File(path);
        if(!htmlFile.exists() & !htmlFile.mkdir())
            throw new FileNotFoundException();
        htmlFile = new File(path + "icon/");
        if(htmlFile.exists() || htmlFile.mkdir()) {
            createRecourse("icon/Youtube.png", path + "icon/Youtube.png");
            createRecourse("icon/Calendar.png", path + "icon/Calendar.png");
            createRecourse("style.css", path + "style.css");
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        DOMImplementation domImpl = document.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("doctype","","");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
        htmlFile = new File(path + "index.html");
        FileOutputStream outStream = new FileOutputStream(htmlFile);
        if(!htmlFile.exists() & !htmlFile.mkdir())
            throw new FileNotFoundException();
        transformer.transform(new DOMSource(document), new StreamResult(outStream));
        Desktop.getDesktop().browse(htmlFile.toURI());
    }
    private Element generateHeader() {
        Element title = document.createElement("title");
        title.appendChild(document.createTextNode(Lang.APPLICATION_NAME));
        Element meta = document.createElement("meta");
        meta.setAttribute("http-equiv", "Content-Type");
        meta.setAttribute("content", "text/html; charset=UTF-8");
        Element link = document.createElement("link");
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("type", "text/css");
        link.setAttribute("href", "style.css");
        Element head = document.createElement("head");
        head.appendChild(meta);
        head.appendChild(link);
        head.appendChild(title);
        return head;
    }
    private Element generateAccount(Service service) throws Exception {
        Map<String, Boolean> settings = new HashMap<>();
        for (Setting setting : this.settings)
            settings.put(setting.name, setting.value);

        Element section = document.createElement("section");
        //Calendar
        if (settings.get("calendar")) {
            Element ul = document.createElement("ul");
            for (CalendarItemElement entry : service.getCalendar().getCalendars()) {
                Element ul2 = document.createElement("ul");
                for (Event entry2 : entry.getEvents()) {
                    Element li2 = document.createElement("li");
                    li2.appendChild(document.createTextNode(entry2.getSummary()));
                    ul2.appendChild(li2);
                }
                Element li = document.createElement("li");
                li.appendChild(document.createTextNode(entry.getCalendar().getSummary()));
                li.appendChild(ul2);
                ul.appendChild(li);
            }
            section.appendChild(generateCard(ul,"icon/Calendar.png", Lang.CALENDAR));
        }

        //Youtube
        if (settings.get("like")||settings.get("dislike")||settings.get("subscription")||settings.get("playlist")) {
            Element youtube = document.createElement("div");
            //like
            if (settings.get("like")) {
                Element ul = document.createElement("ul");
                for (String entry : service.getYoutube().getLikes()) {
                    Element li = document.createElement("li");
                    li.appendChild(document.createTextNode(entry));
                    ul.appendChild(li);
                }
                youtube.appendChild(generateCard(ul,"icon/Youtube.png", Lang.LIKES));
            }
            //dislike
            if (settings.get("dislike")) {
                Element ul = document.createElement("ul");
                for (String entry : service.getYoutube().getDislikes()) {
                    Element li = document.createElement("li");
                    li.appendChild(document.createTextNode(entry));
                    ul.appendChild(li);
                }
                youtube.appendChild(generateCard(ul,"icon/Youtube.png", Lang.DISLIKES));
            }
            //subscription
            if (settings.get("subscription")) {
                Element ul = document.createElement("ul");
                for (Subscription entry : service.getYoutube().getSubscriptions()) {
                    Element li = document.createElement("li");
                    li.appendChild(document.createTextNode(entry.getSnippet().getResourceId().getChannelId()));
                    ul.appendChild(li);
                }
                youtube.appendChild(generateCard(ul,"icon/Youtube.png", Lang.SUBSCRIPTIONS));
            }
            //playlist
            if (settings.get("playlist")) {
                Element ul = document.createElement("ul");
                for (PlaylistElement entry : service.getYoutube().getPlaylists()) {
                    Element ul2 = document.createElement("ul");
                    for (PlaylistItem entry2 : entry.getItems()) {
                        Element li2 = document.createElement("li");
                        li2.appendChild(document.createTextNode(entry2.getSnippet().getResourceId().getVideoId()));
                        ul2.appendChild(li2);
                    }
                    Element li = document.createElement("li");
                    li.appendChild(document.createTextNode(entry.getPlaylist().getSnippet().getTitle()));
                    li.appendChild(ul2);
                    ul.appendChild(li);
                }
                youtube.appendChild(generateCard(ul,"icon/Youtube.png", Lang.PLAYLISTS));
            }
            section.appendChild(youtube);
        }

        Element h1 = document.createElement("h1");
        h1.appendChild(document.createTextNode(service.getUserName()));
        Element p = document.createElement("p");
        p.appendChild(document.createTextNode(service.getUserMail()));
        Element span = document.createElement("div");
        span.appendChild(h1);
        span.appendChild(p);
        Element img = document.createElement("img");
        Image image = service.getUserPhoto();
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
        img.setAttribute("src", "data:image/png;base64, "+encodedString);
        Element header = document.createElement("header");
        header.appendChild(img);
        header.appendChild(span);
        Element main = document.createElement("main");
        main.appendChild(header);
        main.appendChild(section);
        return main;
    }
    private Element generateCard(Element element, String icon, String text) {
        Element span = document.createElement("span");
        span.appendChild(document.createTextNode(text));
        Element img = document.createElement("img");
        img.setAttribute("src", icon);
        Element info = document.createElement("aside");
        info.appendChild(img);
        info.appendChild(span);
        Element content = document.createElement("div");
        content.appendChild(element);
        Element article = document.createElement("article");
        article.appendChild(info);
        article.appendChild(content);
        return article;
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
    private boolean showQuestion(String message){
        int integr = JOptionPane.showConfirmDialog(null,
                message,
                Lang.APPLICATION_NAME,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(integr != 0 & integr != 1)
            exit(0);
        return 0 == integr;
    }
    private void showInfo(String message){
        int integr = JOptionPane.showConfirmDialog(null,
                message,
                Lang.APPLICATION_NAME,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if(integr != 0)
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
    private static class Setting {
        public String name;
        public boolean value;
        public Setting(String type) {
            this.name = type;
            this.value = false;
        }
    }
}
