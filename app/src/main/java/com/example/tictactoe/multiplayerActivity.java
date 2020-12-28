package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class multiplayerActivity extends AppCompatActivity {

    //here create object of buttons for the X/O

    String playerName = "";
    String roomName = "";
    String role = "";
    String message = "";

    FirebaseDatabase database;
    DatabaseReference messageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        //here do the button findviewbyid

        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName = preferences.getString("playerName","");

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            roomName = extras.getString("roomName");
            if (roomName.equals(playerName)){
                role = "host";
            } else {
                role = "guest";
                }
            }

        //put the button onclicklisteners here

            // the next comment is for when you want to send a message that contains data to the other player

            ///to send the data to the other side write:
            /// message = role + "the string you want to send";
            /// messageRef.setValue(message);

        // and this listens to incoming messages
            /// messageRef = database.getReference("rooms/"+roomName+"/message");
            /// message = role + "the string you want to send";
            /// messageRef.setValue(message);
            /// addRoomEventListener();
        addRoomEventListener();
        }

    private void addRoomEventListener() {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if message received
                if (role.equals("host")){
                    if (snapshot.getValue(String.class).contains("guest:")){
                        //enables buttons for the other side
                        //button.setEnabled(true);
                        Toast.makeText(multiplayerActivity.this,""+snapshot.getValue(String.class).replace("guest:",""),Toast.LENGTH_SHORT).show();
                    } else {
                        if (snapshot.getValue(String.class).contains("host:")){
                            //enables buttons for the other side
                            //button.setEnabled(true);
                            Toast.makeText(multiplayerActivity.this,""+snapshot.getValue(String.class).replace("host:",""),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error-retry
                messageRef.setValue(message);
            }
        });
    }

}
