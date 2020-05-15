package fr.docjyJ.googleTransfer.api.Utils;

public class TemplateObject<Object> {
    String id;
    String name;
    Object object;

    public TemplateObject(String id, String name, Object object) {
        this.id = id;
        this.name = name;
        this.object = object;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Object getObject() {
        return object;
    }
}
