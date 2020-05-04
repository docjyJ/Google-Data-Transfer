package fr.docjyJ.googleTransfer.api.Services.calendar;

import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalendarItemElement {
    //ELEMENT
    protected Calendar calendar;
    protected CalendarListEntry calendarList;
    protected List<Event> events;

    //CONSTRUCTOR
    protected CalendarItemElement(CalendarListEntry calendar, com.google.api.services.calendar.Calendar client) throws IOException {
        CalendarElement.logPrint("READ: calendar-[" + calendar.getSummary() + "]");
        if(calendar.getSummary().equals(calendar.getId())) this.calendar = new Calendar()
                .setSummary("primary")
                .setDescription(calendar.getDescription())
                .setLocation(calendar.getLocation())
                .setTimeZone(calendar.getTimeZone());
        else this.calendar = new Calendar()
                .setSummary(calendar.getSummary())
                .setDescription(calendar.getDescription())
                .setLocation(calendar.getLocation())
                .setTimeZone(calendar.getTimeZone());
        this.calendarList = new CalendarListEntry()
                .setBackgroundColor(calendar.getBackgroundColor())
                .setColorId(calendar.getColorId())
                .setDefaultReminders(calendar.getDefaultReminders())
                .setForegroundColor(calendar.getForegroundColor())
                .setHidden(calendar.getHidden())
                .setNotificationSettings(calendar.getNotificationSettings())
                .setSelected(calendar.getSelected())
                .setSummaryOverride(calendar.getSummaryOverride());
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
                CalendarElement.logPrint("READ: calendar-[" + calendar.getSummary() + "]-[" + value.getSummary() + "]");
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
    public CalendarListEntry getCalendarListUpdate() {
        return calendarList;
    }
    public List<Event> getEvents() {
        return events;
    }
}
