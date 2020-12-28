package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class roomSelectionActivity extends AppCompatActivity {

    ListView listView;
    Button findRoomButton;

    List<String> roomsList;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
//    DatabaseReference roomsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomselection);

        database = FirebaseDatabase.getInstance();

        //gets the player name and assigns him a room name with the player name
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName = preferences.getString("playerName","");
        roomName = playerName;

        listView = findViewById(R.id.ListView);
        findRoomButton = findViewById(R.id.findRoomButton);

        //all existing available rooms
        roomsList = new ArrayList<>();

        findRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creates a room and adds the current user as player1
                findRoomButton.setText("Creating Room...");
                findRoomButton.setEnabled(false);
                roomName = playerName;
                roomRef = database.getReference("rooms/"+roomName+"/player1");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //joins an existing room and adds the current user as player2
                roomName = roomsList.get(position);
                roomRef = database.getReference("rooms/"+roomName+"/player2");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

        //shows if new rooms are available
//        addRoomsEventListener();
    }
    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //join the room
                findRoomButton.setText("Create Room...");
                findRoomButton.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(),multiplayerActivity.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error
                findRoomButton.setText("Create Room...");
                findRoomButton.setEnabled(true);
                Toast.makeText(roomSelectionActivity.this,"ERROR!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // this function was supposed to do show list of available rooms but it has been doing some wonky stuff so it's commented for now

   /* private  void   addRoomsEventListener(){
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //shows a list of available rooms
                roomsList.clear();
                Iterable<DataSnapshot> rooms = snapshot.getChildren();
                for (DataSnapshot dataSnapshot : rooms)(
                        roomsList.add(dataSnapshot.getKey())
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(roomSelectionActivity.this,R.layout.support_simple_spinner_dropdown_item, roomsList);
                        listView.setAdapter(adapter);)

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error do nothing
            }
        });
    }*/
}