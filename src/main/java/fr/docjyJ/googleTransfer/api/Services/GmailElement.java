package fr.docjyJ.googleTransfer.api.Services;

import com.google.api.client.util.ArrayMap;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Filter;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListFiltersResponse;
import fr.docjyJ.googleTransfer.api.Utils.GoogleTransfer;
import fr.docjyJ.googleTransfer.api.Utils.TemplateObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GmailElement extends GoogleTransfer<Gmail> {
    //ELEMENT
    protected List<TemplateObject<Label>> labels;
    protected List<TemplateObject<Filter>> filters;
    protected transient Map<String,String> labelCorrection;

    //CONSTRUCTOR
    public GmailElement(Gmail gmail) {
        super(gmail);
    }

    //READ
    public GmailElement readAll() throws IOException {
        return this.readLabels().readFilters();
    }
    public GmailElement readLabels() throws IOException {
        this.labels = new ArrayList<>();
        for (Label label : this.service.users().labels()
                .list("me")
                .execute()
                .getLabels()) {
            if(label.getType().equals("user")) {
                logPrint("READ", "label", label.getName());
                this.labels.add(new TemplateObject<>(
                        label.getId(),
                        label.getName(),
                        new Label()
                                .setLabelListVisibility(label.getLabelListVisibility())
                                .setMessageListVisibility(label.getMessageListVisibility())
                                .setName(label.getName())
                                .setColor(label.getColor())));
            }
        }
        return this;
    }
    public GmailElement readFilters() throws IOException {
        this.filters = new ArrayList<>();
        ListFiltersResponse request = this.service
                .users().settings().filters()
                .list("me")
                .execute();
        for (Filter filter : Objects.requireNonNull(request.getFilter())) {
            logPrint("READ", "filter", filter.getCriteria().toString() + ">" + filter.getAction().toString());
            this.filters.add(new TemplateObject<>(
                    filter.getId(),
                    filter.getCriteria().toString() + ">" + filter.getAction().toString(),
                    new Filter()
                            .setCriteria(filter.getCriteria())
                            .setAction(filter.getAction())));
        }
        return this;
    }

    //PUT
    public GmailElement putAll(GmailElement data) throws IOException {
        return this.putLabels(data.getLabels()).putFilters(data.getFilters());
    }
    public GmailElement putLabels(List<TemplateObject<Label>> data) throws IOException {
        this.labelCorrection = new ArrayMap<>();
        for (TemplateObject<Label> label: data) {
            logPrint("PUT", "label",label.getName());
            this.labelCorrection.put(
                    label.getId(),
                    service.users().labels()
                            .create("me", label.getObject())
                            .execute()
                            .getId()
            );
        }
        return this;
    }
    public GmailElement putFilters(List<TemplateObject<Filter>> data) throws IOException {
        for (TemplateObject<Filter> filter: data) {
            logPrint("PUT", "filter",filter.getName());
            service.users().settings().filters()
                    .create("me", filter.getObject())
                    .execute();
        }
        return this;
    }

    //GET
    public List<TemplateObject<Label>> getLabels() {
        return labels;
    }
    public List<TemplateObject<Filter>> getFilters() {
        return filters;
    }
}
