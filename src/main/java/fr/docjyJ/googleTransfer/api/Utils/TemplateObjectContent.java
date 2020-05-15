package fr.docjyJ.googleTransfer.api.Utils;

import java.util.List;

public class TemplateObjectContent<Object,Content> extends TemplateObject<Object> {
    List<TemplateObject<Content>> content;

    public TemplateObjectContent(String id, String name, Object object, List<TemplateObject<Content>> content) {
        super(id,name,object);
        this.content = content;
    }

    public List<TemplateObject<Content>> getContent() {
        return content;
    }
}
