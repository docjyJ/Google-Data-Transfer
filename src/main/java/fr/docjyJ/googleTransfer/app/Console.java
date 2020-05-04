package fr.docjyJ.googleTransfer.app;


import javax.swing.*;
import java.io.OutputStream;

public class Console extends OutputStream {
    private final JTextArea textArea;

    public Console(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
        // keeps the textArea up to date
        textArea.update(textArea.getGraphics());
    }
}
