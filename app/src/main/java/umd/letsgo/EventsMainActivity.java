package umd.letsgo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;

public class EventsMainActivity extends ListActivity {

    private static final String TAG = "Event Interface";

    EventAdapter mAdapter;
    private static final int ADD_TODO_ITEM_REQUEST = 0;
//    private final List<Event> listEvents = new ArrayList<Event>();
    public static int ID = 1250;
    HashMap<Integer, Event> listEvents=new HashMap<Integer, Event>();
    Firebase ref = new Firebase("https://letsgo436.firebaseio.com");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_events_main);

//        TODO from login get intent and grabed user created
//        Intent intentUser = getIntent();
//        User localUser = (User) intentUser.getSerializableExtra("userObject");


        mAdapter = new EventAdapter(getApplicationContext());

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        TextView footerView = (TextView) this.getLayoutInflater().inflate(R.layout.footer_view, null);

        getListView().addFooterView(footerView);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getBaseContext(), CreateEventActivity.class);
                startActivityForResult(newIntent, ADD_TODO_ITEM_REQUEST);
            }
        });

        getListView().setAdapter(mAdapter);

//        NEED TO ADD STATE TO WORK WITH RESUME
//
//                LOAD EVENTS AND DISPLAY ON PAUSE


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "Entered onActivityResult()");

        // TODO - Check result code and request code
        // if user submitted a new Event Item
        // Create a new Event from the data Intent
        // and then add it to the adapter
        if (requestCode == ADD_TODO_ITEM_REQUEST && resultCode == RESULT_OK) {

            //if (mAdapter.getItem())
            Event newEvent = new Event(data, getApplicationContext());
            //System.out.println(newEvent.toString());
            //ID++;
            //newEvent.setEventID(ID);
            //listEvents.put(ID, newEvent);

            Firebase alanRef = ref.child("events").push();
            alanRef.setValue(newEvent);
            mAdapter.add(newEvent, alanRef.getKey());

            //ADD EVENTS TO FIREBASE


            Toast.makeText(this, R.string.success_create_event, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, R.string.fail_create_event, Toast.LENGTH_LONG).show();
        }


    }
}
