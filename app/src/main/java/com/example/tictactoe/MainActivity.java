package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity ;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private TextView playeronescore,playertwoscore,playerstatus,DrawScore;
private Button [] buttons  =new Button[9];
private Button resetgame;
private int playeronescorecount,playertwoscorecount,numberofrounds,drawscore;
boolean activeplayer;
//we will put 2 as empty,,0 as player 1,,1 as player 2
int [] gamestate={2,2,2,2,2,2,2,2,2}; //this means the square is empty.
int[][] winningpositions={
        {0,1,2},{3,4,5},{6,7,8}, //rows
        {0,3,6},{1,4,7},{2,5,8}, //columns
        {0,4,8},{2,4,6} //cross
};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        activeplayer=true;
    }

    @Override
    public void onClick(View v) {
        //the following if condition make sure that the button pressed only one
if(!((Button)v).getText().toString().equals(""))
{
    return;
}
String buttonId=v.getResources().getResourceEntryName(v.getId());//to get the button id
int gameStatePointer=Integer.parseInt(buttonId.substring(buttonId.length()-1,buttonId.length()));//to delete the word btn- and send only the num of the button
        //play!!!
        if(activeplayer)
        {
            ((Button)v).setText("x");
            ((Button)v).setTextColor(Color.parseColor("#CFDAFF"));
            gamestate[gameStatePointer]=0;
        }
        else
        {
            ((Button)v).setText("O");
            ((Button)v).setTextColor(Color.parseColor("#FFFFFF"));
            gamestate[gameStatePointer]=1;
        }
        numberofrounds++;
        if(checkwinner()) //we have a winner
        {
            //////////////////
            if (activeplayer)
            {
                playeronescorecount++;
                updatePLayerScore();
                Toast.makeText(this,"player one won!",Toast.LENGTH_SHORT).show();
                playAgain();
            }
            else
            {
                playertwoscorecount++;
                updatePLayerScore();
                Toast.makeText(this,"player Two won!",Toast.LENGTH_SHORT).show();
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
        else
        {
            activeplayer=!activeplayer; //to switch player
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
        activeplayer=true;
        for (int i=0;i<buttons.length;i++)
        {
           gamestate[i]=2;
           buttons[i].setText("");
        }
    }
}