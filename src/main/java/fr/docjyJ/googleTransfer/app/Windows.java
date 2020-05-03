package fr.docjyJ.googleTransfer.app;

import javax.swing.*;
import java.awt.*;

public class Windows extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem newSession;

    public static void main(String[] args) {
        new Windows();
    }

    Windows() {
        setTitle("Google Data Transfer");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(buildView());
        setJMenuBar(buildMenu());
        setLocationRelativeTo(null);
        setVisible(true);
        this.setVisible(true);
    }

    JMenuBar buildMenu() {
        newSession = new JMenuItem("New Session");
        newSession.addActionListener(event -> {
            try {
                Session session = new Session();
                tabbedPane.add("test", session.setTreeView());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        file = new JMenu("File");
        file.add(newSession);
        menuBar = new JMenuBar();
        menuBar.add(file);
        return menuBar;
    }

    JScrollPane buildView(){
        tabbedPane = new JTabbedPane();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 1));
        mainPanel.add(tabbedPane);
        return new JScrollPane(mainPanel);
    }

}
