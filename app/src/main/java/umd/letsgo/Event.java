package umd.letsgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jeffsadic on 4/22/2016.
 */
public class Event implements Serializable {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    public final static String NAME = "eventName";
    public final static String LOCATION = "eventLocation";
    public final static String DESCRIPTION = "eventDescription";
    public final static String EVENTDATE = "eventDate";
    public final static String LATITUDE = "eventLatitude";
    public final static String LONGITUDE = "eventLongitude";
    public final static String IMAGE = "eventImage";
    public final static String EMAIL = "eventCreatorEmail";


    private String eventName = new String();
    private String eventDescription = new String();
    private String eventLocation = new String();
    private String eventDate = new String();
    private String longitude = new String();
    private String latitude = new String();
    private String image = new String();
    private String owner = new String();
    private String eventID;

    HashMap<String, String> members =new HashMap<String, String>();

    public HashMap<String, String> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, String> members) {
        this.members = members;
    }




    Event(){

    }
    Event(String eventName, String eventLocation, String eventDescription, String eventDate,
          String longitude, String latitude, String image, String email) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
        this.setOwner(email);
        members.put("email",email);
    }

    Event(Intent intent, Context mContext) {
        this.eventName = intent.getStringExtra(Event.NAME);
        this.eventLocation = intent.getStringExtra(Event.LOCATION);
        this.eventDescription = intent.getStringExtra(Event.DESCRIPTION);
        this.longitude = intent.getStringExtra(Event.LONGITUDE);
        this.latitude = intent.getStringExtra(Event.LATITUDE);
        this.eventDate = intent.getStringExtra(Event.EVENTDATE);
        //members.put(intent.getStringExtra(Event.EMAIL),true);


        String uriString = intent.getStringExtra(Event.IMAGE);
        Uri uri = Uri.parse(uriString);
        Bitmap image = null;
        try {
            InputStream is = mContext.getContentResolver().openInputStream(uri);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        this.image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

    }

    public static void packageIntent(Intent intent, String eventName, String eventLocation,
                                     String eventDescription, String eventDate,
                                     String longitude, String latitude, String image) {

        Log.d("package Intent ", "name :" + eventName +"eventLocation :" + eventLocation
                +"eventDescription :" + eventDescription +"eventDate :" + eventDate
                +"longitude :" + longitude +"Latitude :" + latitude);


        intent.putExtra(Event.NAME, eventName);
        intent.putExtra(Event.LOCATION, eventLocation);
        intent.putExtra(Event.DESCRIPTION, eventDescription);
        intent.putExtra(Event.LONGITUDE, longitude);
        intent.putExtra(Event.LATITUDE, latitude);
        intent.putExtra(Event.EVENTDATE, eventDate);
        intent.putExtra(Event.IMAGE, image);
        //byte[] bytes = image.getBytes(Charset.defaultCharset());
        //String text = new String(bytes, Charsets.UTF_8);


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void addMembers(String id,String email){
        members.put(id,email);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }




}
