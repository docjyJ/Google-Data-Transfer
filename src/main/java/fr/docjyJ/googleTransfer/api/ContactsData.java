package fr.docjyJ.googleTransfer.api;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

import static fr.docjyJ.googleTransfer.Lang.systemLog;
import static fr.docjyJ.googleTransfer.Lang.systemLogError;


public class ContactsData {
    public static void transferContacts(ContactsService clientA, ContactsService clientB)
            throws ServiceException, IOException {
        URL URL = new URL("https://www.google.com/m8/feeds/contacts/default/full");

        ContactFeed resultFeed = clientA.getFeed(URL, ContactFeed.class);
        for (ContactEntry value : resultFeed.getEntries()) {
            try {
                systemLog(value.toString());
                clientB.insert(URL, value);
            }
            catch (InvalidEntryException e){
                systemLogError(e);
            }
        }
    }
}
