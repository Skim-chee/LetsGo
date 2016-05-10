package umd.letsgo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EventsMainActivity extends ListActivity {

    private static final String TAG = "Event Interface";
    private boolean firstTime = true;

    EventAdapter mAdapter;
    private static final int ADD_TODO_ITEM_REQUEST = 0;
//    private final List<Event> listEvents = new ArrayList<Event>();
    public static int ID = 1250;
    HashMap<Integer, Event> listEvents=new HashMap<Integer, Event>();
    Firebase ref = new Firebase("https://letsgo436.firebaseio.com");
    User currentUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_events_main);

//        TODO from login get intent and grabed user created
//        Intent intentUser = getIntent();
//        User localUser = (User) intentUser.getSerializableExtra("userObject");

        Intent received = getIntent();
        currentUser = (User) received.getSerializableExtra("userObject");
        
        mAdapter = new EventAdapter(getApplicationContext());

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        TextView footerView = (TextView) this.getLayoutInflater().inflate(R.layout.footer_view, null);

        getListView().addHeaderView(footerView);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getBaseContext(), CreateEventActivity.class);
                startActivityForResult(newIntent, ADD_TODO_ITEM_REQUEST);
            }
        });

        getListView().setAdapter(mAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                long viewId = view.getId();
                if (viewId == R.id.join_event_button) {
                    Event o = (Event) getListView().getItemAtPosition(position);
                    //Toast.makeText(getBaseContext(), o.getEventName(), Toast.LENGTH_SHORT).show();
                    clickJoin(view);
                }

            }
        });
        if (firstTime){
            loadOngoingEvents();
            firstTime = false;
        }

    }

    public void clickJoin(View v) {
        //not sure if it is just a reference or i get a copy of element.
        final int position = getListView().getPositionForView(v);
        Event pickedEvent =(Event) mAdapter.getItem(position-1);
        if (pickedEvent.getOwner().equals(currentUser.getEmail())){
            Toast.makeText(this.getBaseContext(), "You are the owner of this event." , Toast.LENGTH_SHORT).show();

        } else if (pickedEvent.getMembers().containsKey(currentUser.getId())){
            //check if part of this event already
            Toast.makeText(this.getBaseContext(), "You are part of this event already." , Toast.LENGTH_SHORT).show();
        } else {
            pickedEvent.addMembers(currentUser.getId(), currentUser.getEmail());
            Map<String, Object> newMember = new HashMap<String, Object>();
            newMember.put(currentUser.getId(), currentUser.getEmail());
            ref.child("events/"+ pickedEvent.getEventID() + "/members").updateChildren(newMember);
            Toast.makeText(this.getBaseContext(), "Joined " + pickedEvent.getEventName() + " Event" , Toast.LENGTH_SHORT).show();
        }

        //getListView().getItemAtPosition(position);
    }

    protected void loadOngoingEvents(){
       //Firebase ref2 = new Firebase("https://letsgo436.firebaseio.com/events");
        // Attach an listener to read the data at our posts reference
        ref.child("events").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                mAdapter.clear();
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
            newEvent.setOwner(currentUser.getEmail());
            newEvent.addMembers(currentUser.getId(), currentUser.getEmail());

            //creates initial event
            Firebase alanRef = ref.child("events").push();
            alanRef.setValue(newEvent);

            mAdapter.add(newEvent, alanRef.getKey());
//            mAdapter.notifyDataSetChanged();

            //Reload EVENTS TO FIREBASE CAN BE IMPROVED
            loadOngoingEvents();


            Toast.makeText(this, R.string.success_create_event, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, R.string.fail_create_event, Toast.LENGTH_LONG).show();
        }


    }

}
//                    Event post = new Event();
//                    post.setOwner(postSnapshot.child("owner").getValue(String.class));
//                    post.setEventID(postSnapshot.getKey());
//                    post.setEventDate(postSnapshot.child("eventDate").getValue(String.class));
//                    post.setEventDescription(postSnapshot.child("eventDescription").getValue(String.class));
//                    post.setEventLocation(postSnapshot.child("eventLocation").getValue(String.class));
//                    post.setEventName(postSnapshot.child("eventName").getValue(String.class));
//                    post.setImage(postSnapshot.child("image").getValue(String.class));
//                    post.setLatitude(postSnapshot.child("latitude").getValue(String.class));
//                    post.setLongitude(postSnapshot.child("longitude").getValue(String.class));
//
//                    DataSnapshot members = postSnapshot.child("members");
//                    for (DataSnapshot member : members.getChildren()) {
//                        post.addMembers(member. .child("email").getValue(String.class));
//                    }
//postSnapshot.
