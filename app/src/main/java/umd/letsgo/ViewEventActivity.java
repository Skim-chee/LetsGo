package umd.letsgo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewEventActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Event currentEvent = (Event) getIntent().getSerializableExtra("Event");

        final TextView titleView = (TextView) findViewById(R.id.titleEventView);
        titleView.setText(currentEvent.getEventName());

        final TextView dateView = (TextView) findViewById(R.id.dateEventView);
        dateView.setText(currentEvent.getEventDate());

        final TextView descriptionView = (TextView) findViewById(R.id.descEventView);
        descriptionView.setText(currentEvent.getEventDescription());

        final TextView locationView = (TextView) findViewById(R.id.eventLocationView);
        locationView.setText(currentEvent.getEventLocation());

        final TextView getDirectionsToGoogle = (TextView) findViewById(R.id.getDirectionsView);

        final String latitude = currentEvent.getLatitude();
        final String longitude = currentEvent.getLatitude();

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
}
