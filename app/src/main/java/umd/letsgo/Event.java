package umd.letsgo;

import android.content.Intent;
import android.util.Log;

/**
 * Created by jeffsadic on 4/22/2016.
 */
public class Event {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    public final static String NAME = "eventName";
    public final static String LOCATION = "eventLocation";
    public final static String DESCRIPTION = "eventDescription";
    public final static String EVENTDATE = "eventDate";
    public final static String LATITUDE = "eventLatitude";
    public final static String LONGITUDE = "eventLongitude";

    private String eventName = new String();
    private String eventDescription = new String();
    private String eventLocation = new String();
    private String eventDate = new String();
    private String longitude = new String();
    private String latitude = new String();

    Event(String eventName, String eventLocation, String eventDescription, String eventDate,
          String longitude, String latitude) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    Event(Intent intent) {
        this.eventName = intent.getStringExtra(Event.NAME);
        this.eventLocation = intent.getStringExtra(Event.LOCATION);
        this.eventDescription = intent.getStringExtra(Event.DESCRIPTION);
        this.longitude = intent.getStringExtra(Event.LONGITUDE);
        this.latitude = intent.getStringExtra(Event.LATITUDE);
        this.eventDate = intent.getStringExtra(Event.EVENTDATE);
    }

    public static void packageIntent(Intent intent, String eventName, String eventLocation,
                                     String eventDescription, String eventDate,
                                     String longitude, String latitude) {

        Log.d("package Intent ", "name :" + eventName +"eventLocation :" + eventLocation
                +"eventDescription :" + eventDescription +"eventDate :" + eventDate
                +"longitude :" + longitude +"Latitude :" + latitude);


        intent.putExtra(Event.NAME, eventName);
        intent.putExtra(Event.LOCATION, eventLocation);
        intent.putExtra(Event.DESCRIPTION, eventDescription);
        intent.putExtra(Event.LONGITUDE, longitude);
        intent.putExtra(Event.LATITUDE, latitude);
        intent.putExtra(Event.EVENTDATE, eventDate);
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
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

}
