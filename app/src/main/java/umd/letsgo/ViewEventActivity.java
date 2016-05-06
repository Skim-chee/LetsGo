package umd.letsgo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
                Event currentEvent = snapshot.child(eventId).getValue(Event.class);
                progress.dismiss();
                System.out.println("Did IT find it this way Event name: " + currentEvent.getEventName());

//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    if (postSnapshot.getKey().equals(eventId)){
//                        System.out.println("GREATTTT MATCh");
//                    }
//                    Event post = postSnapshot.getValue(Event.class);
//                    //System.out.println(post.getEventName() + " - " + post.getEventDescription());
//                }

                final TextView titleView = (TextView) findViewById(R.id.titleEventView);
                titleView.setText(currentEvent.getEventName());

                //load image

                final ImageView eventPic = (ImageView) findViewById(R.id.imageEventView);
                eventPic.setImageBitmap(base64ToBitmap(currentEvent.getImage()));

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
                prepareListData();
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
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("List of Friends");
//        listDataHeader.add("Now Showing");
//        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> listFriends = new ArrayList<String>();
        listFriends.add("Jeff - jeffreyr@gmail.com");
        listFriends.add("Carlos  df@gmail.com");
        listFriends.add("Ye fd@gmail.com");
        listFriends.add("Mark  marl@gmail.com");

        listDataChild.put(listDataHeader.get(0), listFriends); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


}
