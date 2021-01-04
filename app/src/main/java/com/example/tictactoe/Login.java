package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText editText;
    Button loginButton;

    String playerName = "";

    FirebaseDatabase database;
    DatabaseReference playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.loginText);
        loginButton = findViewById(R.id.loginButton);

        database = FirebaseDatabase.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks if the player exists and gets a reference to that player

                SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                playerName = preferences.getString("playerName", "");
                if (!playerName.equals((""))){
                    playerRef = database.getReference("players/"+playerName);
                    addEventListener();
                    playerRef.setValue("");
                }

                //for logging the player in
                playerName = editText.getText().toString();
                editText.setText("");
                if (!playerName.equals("")){
                    loginButton.setText("LOGGING IN");
                    loginButton.setEnabled(false);
                    playerRef = database.getReference("players/"+playerName);
                    addEventListener();
                    playerRef.setValue("");
                }
                startActivity(new Intent(getApplicationContext(), roomSelectionActivity.class));
            }
        });
    }
    private void addEventListener(){
        //read from database
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //success continue to next screen after saving the player name
                if (!playerName.equals("")){
                    SharedPreferences preferences = getSharedPreferences("PREFS",0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerName", playerName);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), roomSelectionActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //on error
                loginButton.setText("LOG IN");
                loginButton.setEnabled(true);
                Toast.makeText(Login.this,"ERROR!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}