package com.github.nkzawa.socketio.androidchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = "UserListActivity";

    private Context context;

    private Socket mSocket;

    private ListView listView;

    private HashMap<String,String> usernameIdMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        context = this;
        usernameIdMap = new HashMap<String, String>();
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        mUsername = sharedPref.getString("username","");
//        Log.e("your username", mUsername);

        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.on("new user joined", onNewUserJoined);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(context, "An item of the ListView is clicked.", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.off("new user joined", onNewUserJoined);
    }

    public void clickMe(View view){
        Button bt = (Button) view;
        Toast.makeText(this, "Button " + bt.getText().toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("friendname", bt.getText().toString());
        intent.putExtra("friendid", usernameIdMap.get(bt.getText().toString()));
        setResult(RESULT_OK, intent);
        finish();
    }

    private Emitter.Listener onNewUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String mUsername = getIntent().getExtras().getString("username");
                        JSONArray userlist = data.getJSONArray("userlist");
                        Log.e("NOTE",userlist.toString());
                        String[] items = new String[userlist.length()-1];
                        int idx = 0;
                        usernameIdMap.clear();
                        for (int i = 0; i < userlist.length(); i++) {
                            JSONObject user = userlist.getJSONObject(i);
                            String username = user.getString("username");
                            if (!username.equals(mUsername)) {
                                String id = user.getString("id");
                                usernameIdMap.put(username, id);
                                items[idx] = username;
                                idx++;
                            }
                        }
                        ListViewAdapter adapter = new ListViewAdapter(context, R.layout.list_item, items);
                        // Bind data to the ListView
                        listView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
}
