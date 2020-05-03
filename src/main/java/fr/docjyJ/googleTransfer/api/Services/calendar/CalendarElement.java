package fr.docjyJ.googleTransfer.api.Services.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import fr.docjyJ.googleTransfer.api.Utils.GoogleTransfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalendarElement extends GoogleTransfer {
    //ELEMENT
    protected transient Calendar service;
    protected List<CalendarItemElement> calendars;

    //CONSTRUCTOR
    public CalendarElement(Calendar service) {
        this.service = service;
    }

    //READ
    public CalendarElement readAll() throws IOException {
        return this.readCalendars();
    }
    public CalendarElement readCalendars() throws IOException {
        this.calendars = new ArrayList<>();
        CalendarList request = new CalendarList().setNextPageToken(" ");
        while (request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()) {
            request = this.service.calendarList()
                    .list()
                    .setMinAccessRole("owner")
                    .setMaxResults(50)
                    .setPageToken(request.getNextPageToken())
                    .execute();
            for (CalendarListEntry value : request.getItems()) {
                    this.calendars.add(new CalendarItemElement(value, this.service));
            }
        }
        return this;
    }

    //PUT
    public CalendarElement putAll(Calendar newClient) throws IOException {
        return this.putCalendars(newClient);
    }
    public CalendarElement putCalendars(Calendar newClient) throws IOException {
        for (CalendarItemElement calendar: this.calendars) {
            logPrintln(calendar.calendar);
            String id = "primary";
            if(!calendar.calendar.getSummary().equals(id))
                id = newClient.calendars()
                        .insert(calendar.calendar)
                        .execute()
                        .getId();
            logPrintln(calendar.calendar);
            newClient.calendars()
                    .update(id,calendar.calendar)
                    .execute();
            logPrintln(calendar.calendarList);
            newClient.calendarList()
                    .update(id,calendar.calendarList)
                    .setColorRgbFormat(true)
                    .execute();
            for (Event event :calendar.events){
                logPrintln(event);
                newClient.events()
                        .insert(id, event)
                        .setSupportsAttachments(true)
                        .execute();

            }
        }
        return this;
    }

    //GET
    public Calendar getService() {
        return service;
    }
    public List<CalendarItemElement> getCalendars() {
        return calendars;
    }
}
