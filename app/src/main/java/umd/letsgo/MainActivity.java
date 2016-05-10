package umd.letsgo;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

public class MainActivity extends ListActivity {

    private static final String FIREBASE_URL = "https://letsgo436.firebaseio.com";

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make sure we have a mUsername
        setupUsername();

        setTitle("Chatting as " + mUsername);

        // Setup our Firebase mFirebaseRef
        // Setting chat name in firebase as name+address+randomNum
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("ChatRef", MODE_PRIVATE);
//        String eventName = prefs.getString("eventname", null);
//        String address = prefs.getString("address", null);
        String chatId = prefs.getString("chatId", null);
        System.out.println("not null: "+ chatId);

//        if (eventName == null) {
//            eventName = prefs.getString("eventname", "NOEVENTNAME") ;
//            prefs.edit().putString("eventname", eventName).apply();
//        }
//        if (address == null) {
//            address = prefs.getString("address", "NOADDRESS") ;
//            prefs.edit().putString("address", address).apply();
//        }

        if(chatId == null){
            SharedPreferences.Editor editor = prefs.edit();
            chatId = "LostChat";
            editor.putString("chatId", "LostChat");
            editor.apply();
        }
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat").child(chatId);

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(MainActivity.this, "Connected to Chat", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Disconnected from Chat", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("ChatRef", MODE_PRIVATE);
        mUsername = prefs.getString("email", null);
        if (mUsername == null) {
            mUsername = prefs.getString("email", "noname@doesnotexist.com") ;
            prefs.edit().putString("email", mUsername).apply();
        }
    }

    private void sendMessage() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("ChatRef", MODE_PRIVATE);
        int size = prefs.getInt("Users_size", 0);
        String userEmail = prefs.getString("email", null);
        boolean ismember = false;
        if(userEmail != null){
            for(int i = 1; i <= size; i++) {
                String temp = prefs.getString("User_" + i, null);
                if(temp.equals(userEmail)){
                    ismember = true;
                    break;
                }
            }

            if(ismember){
                EditText inputText = (EditText) findViewById(R.id.messageInput);
                String input = inputText.getText().toString();
                if (!input.equals("")) {
                    // Create our 'model', a Chat object
                    Chat chat = new Chat(input, mUsername);
                    // Create a new, auto-generated child of that chat location, and save our chat data there
                    mFirebaseRef.push().setValue(chat);
                    inputText.setText("");
                }
            }else{
                Toast.makeText(getApplicationContext(), "Event not joined. Please join event first.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
