package fr.docjyJ.googleTransfer.api.Utils;

import java.util.List;

public class IdKeyElement {
    String id;
    String name;
    Object object;
    Object object2;
    List<IdKeyElement> content;

    public IdKeyElement(String id, String name, Object object) {
        this.id = id;
        this.name = name;
        this.object = object;
    }
    public IdKeyElement(String id, String name, Object object, List<IdKeyElement> content) {
        this.id = id;
        this.name = name;
        this.object = object;
        this.content = content;
    }
    public IdKeyElement(String id, String name, Object object, Object object2, List<IdKeyElement> content) {
        this.id = id;
        this.name = name;
        this.object = object;
        this.object2 = object2;
        this.content = content;
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
    public Object getObject2() {
        return object2;
    }
    public List<IdKeyElement> getContent() {
        return content;
    }
}
