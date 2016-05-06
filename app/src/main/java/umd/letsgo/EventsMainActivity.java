package umd.letsgo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

public class EventsMainActivity extends ListActivity {

    private static final String TAG = "Event Interface";
    private boolean firstTime = true;

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
        if (firstTime){
            loadOngoingEvents();
            firstTime = false;
        }




//        NEED TO ADD STATE TO WORK WITH RESUME
//
//                LOAD EVENTS AND DISPLAY ON PAUSE


    }
    protected void loadOngoingEvents(){
       //Firebase ref2 = new Firebase("https://letsgo436.firebaseio.com/events");
        // Attach an listener to read the data at our posts reference
        ref.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Event post = postSnapshot.getValue(Event.class);
                    System.out.println(postSnapshot.getKey() + " --- " + post.getEventName());
                    mAdapter.add(post, postSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

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
