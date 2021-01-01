package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class multiplayerActivity extends AppCompatActivity implements View.OnClickListener {

    //here create object of buttons for the X/O
    private TextView playeronescore,playertwoscore,playerstatus,DrawScore;
    private Button [] buttons  =new android.widget.Button[9];
    private Button resetgame;
    private int playeronescorecount,playertwoscorecount,numberofrounds,drawscore;
    int [] gamestate={2,2,2,2,2,2,2,2,2}; //this means the square is empty.
    int[][] winningpositions={
            {0,1,2},{3,4,5},{6,7,8}, //rows
            {0,3,6},{1,4,7},{2,5,8}, //columns
            {0,4,8},{2,4,6} //cross
    };

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

        //here do the findviewbyid
        playeronescore=(TextView)findViewById(R.id.PlayerOneScore);
        playertwoscore=(TextView)findViewById(R.id.PlayerTwoScore);
        playerstatus=(TextView)findViewById(R.id.PlayerStatus);
        DrawScore=(TextView)findViewById(R.id.Draw);
        resetgame=(Button)findViewById(R.id.resetgame);
        for (int i=0;i<buttons.length;i++)
        {
            String buttonid="btn_"+i;
            int resourceid=getResources().getIdentifier(buttonid,"id",getPackageName());
            buttons[i]=(Button)findViewById(resourceid);
            buttons[i].setOnClickListener(this);
        }

        numberofrounds=0;
        playeronescorecount=0;
        playertwoscorecount=0;
        drawscore=0;

        database = FirebaseDatabase.getInstance();
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

        }

    // the next comment is for when you want to send a message that contains data to the other player

    ///to send the data to the other side write:
    /// message = role + "the string you want to send";
    /// messageRef.setValue(message);

    // and this listens to incoming messages
    /// messageRef = database.getReference("rooms/"+roomName+"/message");
    /// message = role + "the string you want to send";
    /// messageRef.setValue(message);
    /// addRoomEventListener();

    @Override
    public void onClick(View v) {
        //the following if condition make sure that the button pressed only one
        if(!((Button)v).getText().toString().equals(""))
        {
            return;
        }

        String buttonId=v.getResources().getResourceEntryName(v.getId());//to get the button id
        String gamestatestring = buttonId.substring(buttonId.length()-1,buttonId.length());
        int gameStatePointer=Integer.parseInt(gamestatestring);//to delete the word btn- and send only the num of the button
        //play!!!
        if(role == "host")
        {
            ((Button)v).setEnabled(false);
            messageRef = database.getReference("rooms/"+roomName+"/message");
            message = role + gamestatestring;
            messageRef.setValue(message);
            addRoomEventListener();
            ((Button)v).setText("x");
            ((Button)v).setTextColor(Color.parseColor("#CFDAFF"));
            gamestate[gameStatePointer]=0;
        }
        else
        {
            messageRef = database.getReference("rooms/"+roomName+"/message");
            message = role + gamestatestring;
            messageRef.setValue(message);
            addRoomEventListener();
            ((Button)v).setText("O");
            ((Button)v).setTextColor(Color.parseColor("#FFFFFF"));
            gamestate[gameStatePointer]=1;

        }
        numberofrounds++;
        if(checkwinner()) //we have a winner
        {
            //////////////////
            if (role == "host")
            {
                playeronescorecount++;
                updatePLayerScore();
                Toast.makeText(this,"player"+playerName+ "won!",Toast.LENGTH_SHORT).show();
                playAgain();
            }
            else
            {
                playertwoscorecount++;
                updatePLayerScore();
                Toast.makeText(this,"opponent won!",Toast.LENGTH_SHORT).show();
                playAgain();
            }
        }
        else if (numberofrounds==9) //no winner
        {
            drawscore++;
            updatePLayerScore();
            Toast.makeText(this,"Draw!",Toast.LENGTH_SHORT).show();
            playAgain();
        }

        //Playerstatus
        if(playeronescorecount > playertwoscorecount){
            playerstatus.setText("Player One is Winning!");
        }
        else if(playertwoscorecount > playeronescorecount){
            playerstatus.setText("Player Two is Winning!");
        }
        else{
            playerstatus.setText("");
        }

        //reset game button
        resetgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                playeronescorecount=0;
                playertwoscorecount=0;
                drawscore=0;
                playerstatus.setText("");
                updatePLayerScore();
            }
        });
    }


    private void addRoomEventListener() {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if message received
                if (role.equals("host")){
                    if (snapshot.getValue(String.class).contains("guest")){
                        //enables buttons for the other side
                        for (int i=0;i<buttons.length;i++)
                        {
                            //button.setEnabled(true);
                            buttons[i].setEnabled(true);
                        }

                        String recievedMessage = snapshot.getValue(String.class).replace("guest","");
                        int gamestateMessage = Integer.parseInt(recievedMessage);
                        buttons[gamestateMessage].setText("O");
                        buttons[gamestateMessage].setTextColor(Color.parseColor("#FFFFFF"));
                        gamestate[gamestateMessage] = 1;

                        Toast.makeText(multiplayerActivity.this,""+snapshot.getValue(String.class).replace("guest:",""),Toast.LENGTH_SHORT).show();

                    } else {
                        if (snapshot.getValue(String.class).contains("host")){

                            //enables buttons for the other side
                            for (int i=0;i<buttons.length;i++)
                            {
                                //button.setEnabled(true);
                                buttons[i].setEnabled(true);
                            }
                            String recievedMessage = snapshot.getValue(String.class).replace("host","");
                            int gamestateMessage = Integer.parseInt(recievedMessage);
                            buttons[gamestateMessage].setText("X");
                            buttons[gamestateMessage].setTextColor(Color.parseColor("#CFDAFF"));
                            gamestate[gamestateMessage] = 0;

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
    public boolean checkwinner()
    {
        boolean winnerResult=false;
        for (int[]winningPosition:winningpositions)
        {
            if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                    gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                    gamestate[winningPosition[0]]!=2)
            {
                winnerResult=true;
            }
        }
        return winnerResult;
    }
    public void updatePLayerScore()
    {
        playeronescore.setText(Integer.toString(playeronescorecount));
        playertwoscore.setText(Integer.toString(playertwoscorecount));
        DrawScore.setText(Integer.toString(drawscore));
    }
    public void playAgain(){
        numberofrounds=0;

        for (int i=0;i<buttons.length;i++)
        {
            gamestate[i]=2;
            buttons[i].setText("");
        }
    }

}
