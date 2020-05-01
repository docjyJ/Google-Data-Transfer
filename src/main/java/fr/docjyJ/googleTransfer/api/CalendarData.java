package fr.docjyJ.googleTransfer.api;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;

import static fr.docjyJ.googleTransfer.Lang.systemLog;
import static fr.docjyJ.googleTransfer.Lang.systemLogError;

public class CalendarData {
    public static void transferCalendars(Calendar clientA, Calendar clientB, String pageToken) throws IOException {
        CalendarList request = clientA.calendarList()
                .list()
                .setMaxResults(50)
                .setPageToken(pageToken)
                .execute();
        for( CalendarListEntry value : request.getItems()) {
            try {
                transferEvent( clientA, clientB, "", value.getId(),
                        clientB.calendars().insert(
                                new com.google.api.services.calendar.model.Calendar()
                                        .setSummary(value.getSummary()))
                                .execute().getId());
            }
            catch (GoogleJsonResponseException e){
                systemLogError(e);
            }
        }
        if(request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()){
            transferCalendars(clientA, clientB, request.getNextPageToken());
        }
    }

    public static void transferEvent(Calendar clientA, Calendar clientB, String pageToken,
                                            String CalendarIdA,String CalendarIdB) throws IOException {
        Events request = clientA.events()
                .list(CalendarIdA)
                .setMaxResults(50)
                .setPageToken(pageToken)
                .execute();
        for( Event value : request.getItems()) {
            try {
                systemLog(value.getId());
                clientB.events().insert(CalendarIdB, new Event()
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
                            .setTransparency(value.getTransparency()))
                        .setSupportsAttachments(true)
                        .execute();
                Thread.sleep(1000);
            }
            catch (GoogleJsonResponseException | InterruptedException e){
                systemLogError(e);
            }
        }
        if(request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()){
            transferEvent(clientA, clientB, request.getNextPageToken(), CalendarIdA, CalendarIdB);
        }
    }

}
