package umd.letsgo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EventsMainActivity extends ListActivity {

    private static final String TAG = "Event Interface";
    EventAdapter mAdapter;
    private static final int ADD_TODO_ITEM_REQUEST = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_events_main);

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
//                LOAD EVENTS AND DISPLAY


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "Entered onActivityResult()");

        // TODO - Check result code and request code
        // if user submitted a new ToDoItem
        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter
        if (requestCode == ADD_TODO_ITEM_REQUEST && resultCode == RESULT_OK) {

            //if (mAdapter.getItem())
            Event newEvent = new Event(data);
            mAdapter.add(newEvent);
            Toast.makeText(this, R.string.success_create_event, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, R.string.fail_create_event, Toast.LENGTH_LONG).show();
        }


    }
}
