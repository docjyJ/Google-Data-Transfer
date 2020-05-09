package fr.docjyJ.googleTransfer.api.Services.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import fr.docjyJ.googleTransfer.api.Utils.GoogleTransfer;
import fr.docjyJ.googleTransfer.api.Utils.IdKeyElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalendarElement extends GoogleTransfer {
    //ELEMENT
    protected transient Calendar service;
    protected List<IdKeyElement> calendars;

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
                CalendarElement.logPrint("READ", "calendar", value.getSummary());
                com.google.api.services.calendar.model.Calendar calendar;
                if(value.getSummary().equals(value.getId()))
                    calendar = new com.google.api.services.calendar.model.Calendar()
                        .setSummary("primary")
                        .setDescription(value.getDescription())
                        .setLocation(value.getLocation())
                        .setTimeZone(value.getTimeZone());
                else
                    calendar = new com.google.api.services.calendar.model.Calendar()
                        .setSummary(value.getSummary())
                        .setDescription(value.getDescription())
                        .setLocation(value.getLocation())
                        .setTimeZone(value.getTimeZone());
                CalendarListEntry calendarList = new CalendarListEntry()
                        .setBackgroundColor(value.getBackgroundColor())
                        .setColorId(value.getColorId())
                        .setDefaultReminders(value.getDefaultReminders())
                        .setForegroundColor(value.getForegroundColor())
                        .setHidden(value.getHidden())
                        .setNotificationSettings(value.getNotificationSettings())
                        .setSelected(value.getSelected())
                        .setSummaryOverride(value.getSummaryOverride());
                List<IdKeyElement> events = new ArrayList<>();
                Events request2 = new Events().setNextPageToken(" ");
                while (request2.getNextPageToken()!=null && !request2.getNextPageToken().isEmpty()) {
                    Calendar.Events.List temp = this.service.events()
                            .list(value.getId())
                            .setMaxResults(50);
                    if(!request2.getNextPageToken().equals(" "))
                        temp.setPageToken(request2.getNextPageToken());
                    request2=temp.execute();
                    for( Event value2 : request2.getItems()) {
                        CalendarElement.logPrint("READ", "calendar",
                                value.getSummary(), value2.getSummary());
                        events.add(new IdKeyElement(
                            value2.getHtmlLink(),
                            value2.getSummary(),
                            new Event()
                                .setStart(value2.getStart())
                                .setEnd(value2.getEnd())
                                .setAnyoneCanAddSelf(value2.getAnyoneCanAddSelf())
                                .setAttachments(value2.getAttachments())
                                .setAttendees(value2.getAttendees())
                                .setColorId(value2.getColorId())
                                .setConferenceData(value2.getConferenceData())
                                .setDescription(value2.getDescription())
                                .setExtendedProperties(value2.getExtendedProperties())
                                .setGadget(value2.getGadget())
                                .setGuestsCanInviteOthers(value2.getGuestsCanInviteOthers())
                                .setGuestsCanModify(value2.getGuestsCanModify())
                                .setGuestsCanSeeOtherGuests(value2.getGuestsCanSeeOtherGuests())
                                .setLocation(value2.getLocation())
                                .setOriginalStartTime(value2.getOriginalStartTime())
                                .setRecurrence(value2.getRecurrence())
                                .setReminders(value2.getReminders())
                                .setSource(value2.getSource())
                                .setStatus(value2.getStatus())
                                .setSummary(value2.getSummary())
                                .setVisibility(value2.getVisibility())
                                .setTransparency(value2.getTransparency())));
                    }
                }
                this.calendars.add(new IdKeyElement(
                        value.getId(),
                        value.getSummary(),
                        calendar,
                        calendarList,
                        events));
            }
        }
        return this;
    }

    //PUT
    public CalendarElement putAll(CalendarElement data) throws IOException {
        return this.putCalendars(data.getCalendars());
    }
    public CalendarElement putCalendars(List<IdKeyElement> data) throws IOException {
        for (IdKeyElement calendar: data) {
            CalendarElement.logPrint("PUT", "calendar", calendar.getName());
            String id = "primary";
            if(!calendar.getName().equals(id))
                id = service.calendars()
                        .insert((com.google.api.services.calendar.model.Calendar) calendar.getObject())
                        .execute()
                        .getId();
            service.calendars()
                    .update(id,(com.google.api.services.calendar.model.Calendar) calendar.getObject())
                    .execute();
            service.calendarList()
                    .update(id, (CalendarListEntry) calendar.getObject2())
                    .setColorRgbFormat(true)
                    .execute();
            for (IdKeyElement event :calendar.getContent()){
                CalendarElement.logPrint("PUT", "calendar",
                        calendar.getName(), event.getName());
                service.events()
                        .insert(id, (Event) event.getObject())
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
    public List<IdKeyElement> getCalendars() {
        return calendars;
    }
}
