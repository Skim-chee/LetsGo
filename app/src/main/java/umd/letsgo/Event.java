package umd.letsgo;

import android.content.Intent;

/**
 * Created by jeffsadic on 4/22/2016.
 */
public class Event {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    public final static String NAME = "eventName";
    public final static String DESCRIPTION = "eventDescription";
    public final static String EVENTDATE = "eventDate";
    public final static String LOCATION = "eventLocation";

    private String eventName = new String();
    private String eventDescription = new String();
    private String eventLocation = new String();
    private String eventDate = new String();

    Event(String eventName, String eventDescription, String eventLocation, String eventDate) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public String getEventDescription() {
        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public String getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    // Create a new ToDoItem from data packaged in an Intent

    Event(Intent intent) {
        this.eventName = intent.getStringExtra(Event.NAME);
        this.eventDescription = intent.getStringExtra(Event.DESCRIPTION);
        this.eventLocation = intent.getStringExtra(Event.LOCATION);
        this.eventDate = intent.getStringExtra(Event.EVENTDATE);
    }

    public static void packageIntent(Intent intent, String eventName, String eventDescription,
                                     String eventLocation, String eventDate) {

        intent.putExtra(Event.NAME, eventName);
        intent.putExtra(Event.DESCRIPTION, eventDescription);
        intent.putExtra(Event.LOCATION, eventLocation);
        intent.putExtra(Event.EVENTDATE, eventDate);
    }

}
