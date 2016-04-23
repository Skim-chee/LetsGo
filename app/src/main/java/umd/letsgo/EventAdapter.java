package umd.letsgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffsadic on 4/11/2016.
 */
public class EventAdapter extends BaseAdapter {


    private final List<Event> mItems = new ArrayList<Event>();
    private final Context mContext;

    private static final String TAG = "Flag Interface";

    public EventAdapter(Context context) {
        mContext  = context;
    }

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    public void add(Event item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO - Get the current ToDoItem
        final Event toDoItem = mItems.get(position);

        // TODO - Inflate the View for this ToDoItem
        // from todo_item.xml
        LayoutInflater newinflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout itemLayout = (RelativeLayout) newinflater.inflate(R.layout.Event, null);

        // Fill in specific ToDoItem data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file

        // TODO - Display Title in TextView
        final TextView placeView = (TextView) itemLayout.findViewById(R.id.PlaceLabel);
        placeView.setText(toDoItem.getPlace());

        // TODO - Display Title in TextView
        final TextView countryView = (TextView) itemLayout.findViewById(R.id.CountryLabel);
        countryView.setText(toDoItem.getCountry());

        final ImageView flagView = (ImageView) itemLayout.findViewById(R.id.imageView);
        flagView.setImageBitmap(toDoItem.getFlagBitmap());

        return null;
    }
}
