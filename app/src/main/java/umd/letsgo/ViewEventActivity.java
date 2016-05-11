package umd.letsgo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class ViewEventActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Firebase ref = new Firebase("https://letsgo436.firebaseio.com/events");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Intent received = getIntent();
        final String eventId = received.getStringExtra("Event");

        //ref = new Firebase("https://letsgo436.firebaseio.com/events/" + eventId);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading Event");
        progress.setMessage("Wait while loading...");
//        progress.setCancelable(false);
        progress.show();

        // To dismiss the dialog

        //addListenerForSingleValueEvent  addValueEventListener
        // Attach an listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");


                //System.out.println("Did IT find it this way" + snapshot.child(eventId).toString());
                //READ FROM STORE AND RECREAT EVENT
                Event currentEvent = snapshot.child(eventId).getValue(Event.class);

                //Olina- gather info for chat name
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("ChatRef", MODE_PRIVATE).edit();
                editor.putString("eventname", currentEvent.getEventName());
                editor.putString("address", currentEvent.getEventLocation());

                //Olina- adding list of user emails
                Collection <String> usersTemp = currentEvent.getMembers().values();
                Object[] users = usersTemp.toArray();
                editor.putInt("Users_size", users.length);
                for(int i = 1; i <= users.length; i++) {
                    editor.remove("User_" + i);
                    editor.putString("User_" + i, users[i - 1].toString());
                }

                //Olina - creating specific event chat id
//                String eventId = currentEvent.getEventID();
//                System.out.println(eventId);
//                editor.putString("eventId", eventId);

                System.out.println("putting: "+eventId);
                editor.putString("chatId", eventId+"chat");
                editor.apply();


                progress.dismiss();
                System.out.println("Did IT find it this way Event name: " + currentEvent.getEventName());

                //PRINTS USERS
                System.out.println(currentEvent.getMembers().values().toString());

                final TextView titleView = (TextView) findViewById(R.id.titleEventView);
                titleView.setText(currentEvent.getEventName());

                //load image

                final ImageView eventPic = (ImageView) findViewById(R.id.imageEventView);
                eventPic.setImageBitmap(base64ToBitmapWithReq(currentEvent.getImage(),256,192));

                final TextView dateView = (TextView) findViewById(R.id.dateEventView);
                dateView.setText(currentEvent.getEventDate());

                final TextView descriptionView = (TextView) findViewById(R.id.descEventView);
                descriptionView.setText(currentEvent.getEventDescription());

                final TextView locationView = (TextView) findViewById(R.id.eventLocationView);
                locationView.setText(currentEvent.getEventLocation());

                final TextView getDirectionsToGoogle = (TextView) findViewById(R.id.getDirectionsView);

                final String latitude = currentEvent.getLatitude();
                final String longitude = currentEvent.getLongitude();


                // get the listview
                expListView = (ExpandableListView) findViewById(R.id.expandableListView);
                // preparing list data
                prepareListData(currentEvent.getMembers());
                listAdapter = new umd.letsgo.ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
                // setting list adapter
                expListView.setAdapter(listAdapter);



                getDirectionsToGoogle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Create a Uri from an intent string. Use the result to create an Intent.
                        String URL = "google.streetview:cbll=" + latitude + "," + longitude;
                        Uri gmmIntentUri = Uri.parse(URL);

                        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        // Make the Intent explicit by setting the Google Maps package
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Google Maps Not found in your Device", Toast.LENGTH_LONG).show();
                        }
                        // Attempt to start an activity that can handle the Intent

                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        //final Button checkButton = (Button) findViewById(R.id.CheckIntentButton);
        final Button chatButton = (Button) findViewById(R.id.chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(ViewEventActivity.this, MainActivity.class);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chatIntent);

            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData(HashMap<String, String> users) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("List of Attendees");
//        listDataHeader.add("Now Showing");
//        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> listFriends = new ArrayList<String>();

        for (String user : users.values()){
            listFriends.add(user);
        }

        listDataChild.put(listDataHeader.get(0), listFriends); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
    private Bitmap base64ToBitmap(String b64) {

        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        Bitmap eventImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        Bitmap eventImageScaled = Bitmap.createScaledBitmap(eventImage, eventImage.getWidth()/2 , eventImage.getHeight()/2 , true);// convert decoded bitmap into well scalled Bitmap format.
        return eventImageScaled;

    }

    private Bitmap base64ToBitmapWithReq(String b64,int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        options.inJustDecodeBounds = true;
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length, options);

    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}