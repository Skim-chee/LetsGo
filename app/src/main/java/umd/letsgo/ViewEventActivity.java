package umd.letsgo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewEventActivity extends Activity {


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

        final TextView locationView = (TextView) findViewById(R.id.titleEventView);
        locationView.setText(currentEvent.getEventName());



        //final Button checkButton = (Button) findViewById(R.id.CheckIntentButton);



    }
}
