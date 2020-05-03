package fr.docjyJ.googleTransfer.Services.gmail;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Filter;
import com.google.api.services.gmail.model.Label;
import fr.docjyJ.googleTransfer.Utils.GoogleTransfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GmailElement extends GoogleTransfer {
    //ELEMENT
    protected transient Gmail service;
    protected List<Filter> filters;
    protected List<Label> labels;
    protected Map<String,String> labelCorrection;

    //CONSTRUCTOR
    public GmailElement(Gmail service) {
        this.service = service;
    }

    //READ
    public GmailElement readAll() throws IOException {
        return this;
    }
    public GmailElement readFilters() throws IOException {
        this.filters = new ArrayList<>();
        this.filters.addAll(this.service
                .users().settings().filters()
                .list("me")
                .execute()
                .getFilter());
        return this;
    }
    public GmailElement readLabels() throws IOException {
        this.labels = new ArrayList<>();
        for (Label label : this.service.users().labels()
                .list("me")
                .execute()
                .getLabels()) {
            if(label.getType().equals("user"))
                this.labels.add(label);
        }
        return this;
    }

    //PUT
    public GmailElement putAll(Gmail newClient) throws IOException {
        return this;
    }
    public GmailElement putLabels(Gmail newClient) throws IOException {
        for (Label label:this.labels) {
            this.labelCorrection.put(
                    label.getId(),
                    newClient.users().labels()
                            .create("me", new Label()
                                    .setName(label.getName())
                                    .setLabelListVisibility(label.getLabelListVisibility())
                                    .setMessageListVisibility(label.getMessageListVisibility())
                                    .setColor(label.getColor()))
                            .execute()
                            .getId()
            );
        }
        return this;
    }



    //GET
    public Gmail getService() {
        return service;
    }
    public List<Filter> getFilters() {
        return filters;
    }
}
