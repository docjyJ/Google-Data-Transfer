package fr.docjyJ.googleTransfer.Services.calendar;

import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalendarItemElement {
    //ELEMENT
    Calendar calendar;
    List<Event> events;

    //CONSTRUCTOR
    protected CalendarItemElement(CalendarListEntry calendar, com.google.api.services.calendar.Calendar client) throws IOException {
        this.calendar = new Calendar()
                .setSummary(calendar.getSummary());
        this.events = new ArrayList<>();
        Events request = new Events().setNextPageToken(" ");
        while (request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()) {
            com.google.api.services.calendar.Calendar.Events.List temp = client.events()
                    .list(calendar.getId())
                    .setMaxResults(50);
            if(!request.getNextPageToken().equals(" "))
                temp.setPageToken(request.getNextPageToken());
            request=temp.execute();
            for( Event value : request.getItems()) {
                this.events.add(new Event()
                        .setStart(value.getStart())
                        .setEnd(value.getEnd())
                        .setAnyoneCanAddSelf(value.getAnyoneCanAddSelf())
                        .setAttachments(value.getAttachments())
                        .setAttendees(value.getAttendees())
                        .setColorId(value.getColorId())
                        .setConferenceData(value.getConferenceData())
                        .setDescription(value.getDescription())
                        .setExtendedProperties(value.getExtendedProperties())
                        .setGadget(value.getGadget())
                        .setGuestsCanInviteOthers(value.getGuestsCanInviteOthers())
                        .setGuestsCanModify(value.getGuestsCanModify())
                        .setGuestsCanSeeOtherGuests(value.getGuestsCanSeeOtherGuests())
                        .setLocation(value.getLocation())
                        .setOriginalStartTime(value.getOriginalStartTime())
                        .setRecurrence(value.getRecurrence())
                        .setReminders(value.getReminders())
                        .setSource(value.getSource())
                        .setStatus(value.getStatus())
                        .setSummary(value.getSummary())
                        .setVisibility(value.getVisibility())
                        .setTransparency(value.getTransparency()));
            }
        }
    }

    //GET
    public Calendar getCalendar() {
        return calendar;
    }
    public List<Event> getEvents() {
        return events;
    }
}
