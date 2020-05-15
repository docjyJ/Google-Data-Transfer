package fr.docjyJ.googleTransfer.api.Services;

import com.google.api.services.people.v1.PeopleService;
import com.google.gdata.data.Person;
import fr.docjyJ.googleTransfer.api.Utils.GoogleTransfer;

import java.util.ArrayList;
import java.util.List;

public class ContactElement extends GoogleTransfer<PeopleService> {
    //ELEMENT
    protected List<Person> contacts;

    //CONSTRUCTOR
    public ContactElement(PeopleService peopleService) {
        super(peopleService);
    }

    //READ
    public ContactElement readAll() {
        return this.readContacts();
    }
    public ContactElement readContacts() {
        this.contacts = new ArrayList<>();
        return this;
    }


    //PUT
    public ContactElement putAll(ContactElement data) {
        return this.putContacts(data.getContacts());
    }
    public ContactElement putContacts(List<Person> data) {
        return this;
    }

    //GET
    public List<Person> getContacts() {
        return contacts;
    }
}
