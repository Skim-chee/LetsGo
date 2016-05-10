package umd.letsgo;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
    }

    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        TextView authorInput = (TextView) view.findViewById(R.id.author);
        authorInput.setText(author + ": ");
        // If autho's message blue, other messages are grey.
        if (author != null && author.equals(mUsername)) {
            authorInput.setTextColor(Color.BLUE);
        } else {
            authorInput.setTextColor(Color.GRAY);
        }
        ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());
    }
}
