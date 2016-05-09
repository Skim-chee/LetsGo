package umd.letsgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffsadic on 4/11/2016.
 */
public class EventAdapter extends BaseAdapter {


    private final List<Event> mItems = new ArrayList<Event>();
    //HashMap<String, Event> listEvents =new HashMap<String, Event>();
    private final Context mContext;

    private static final String TAG = "Flag Interface";

    public EventAdapter(Context context) {
        mContext  = context;
    }

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    public void add(Event item, String id) {
        item.setEventID(id);
        mItems.add(item);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    private Bitmap base64ToBitmap(String b64) {
        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        Bitmap eventImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        Bitmap eventImageScaled = Bitmap.createScaledBitmap(eventImage, 150 , 100 , true);// convert decoded bitmap into well scalled Bitmap format.
        return eventImageScaled;
//        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO - Get the current ToDoItem
        final Event event = mItems.get(position);

        // TODO - Inflate the View for this ToDoItem
        // from todo_item.xml
        LayoutInflater newinflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout itemLayout = (RelativeLayout) newinflater.inflate(R.layout.event_view, null);

        // TODO - Display Title in TextView
        final TextView nameView = (TextView) itemLayout.findViewById(R.id.event_name_textView);
        nameView.setText(event.getEventName());

        // TODO - Display Title in TextView
        final TextView countryView = (TextView) itemLayout.findViewById(R.id.event_date_textView);
        countryView.setText(event.getEventDate());

        final ImageView eventPic = (ImageView) itemLayout.findViewById(R.id.imageViewListEvents);
        eventPic.setImageBitmap(base64ToBitmap(event.getImage()));

        final Button viewEventView = (Button) itemLayout.findViewById(R.id.view_event_button);

        viewEventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do something
                //send intent to view event activity with user and event object
                Intent newIntent = new Intent(mContext, ViewEventActivity.class);
                newIntent.putExtra("Event", ((Event) getItem(position)).getEventID());
                //getIntent().getSerializableExtra("MyClass");
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(newIntent);
                notifyDataSetChanged();
            }
        });

        final Button joinEventView = (Button) itemLayout.findViewById(R.id.join_event_button);

        joinEventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();
            }
        });

        return itemLayout;
    }
}
