package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity ;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private TextView playeronescore,playertwoscore,playerstatus,DrawScore,PlayerOne,PlayerTwo;
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
        PlayerOne=(TextView)findViewById(R.id.PlayerOne);
        PlayerTwo=(TextView)findViewById(R.id.PlayerTwo);
        playeronescore=(TextView)findViewById(R.id.PlayerOneScore);
        playertwoscore=(TextView)findViewById(R.id.PlayerTwoScore);
        playerstatus=(TextView)findViewById(R.id.PlayerStatus);
        DrawScore=(TextView)findViewById(R.id.DrawScore);
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
            ((Button)v).setText("X");
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
                playeronescorecount=0;
                playertwoscorecount=0;
                drawscore=0;
                playerstatus.setText("");
                updatePLayerScore();
                for (int i=0;i<buttons.length;i++) {
                    gamestate[i] = 2;
                    buttons[i].setText("");
                    buttons[i].setBackgroundColor(Color.parseColor("#148F77"));
                }
            }
        });
    }
    public boolean checkwinner()
    {
       boolean winnerResult=false;
       int i=0;
       for (int[]winningPosition:winningpositions)
       {
          if(i==0)
          {
              if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                      gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                      gamestate[winningPosition[0]]!=2)
              {
                  buttons[0].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[1].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[2].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  winnerResult=true;
              }
          }else  if(i==1)
          {
              if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                      gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                      gamestate[winningPosition[0]]!=2)
              {
                  buttons[3].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[4].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[5].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  winnerResult=true;
              }
          }
          else  if(i==2)
          {
              if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                      gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                      gamestate[winningPosition[0]]!=2)
              {
                  buttons[6].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[7].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[8].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  winnerResult=true;
              }
          }
          else  if(i==3)
          {
              if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                      gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                      gamestate[winningPosition[0]]!=2)
              {
                  buttons[0].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[3].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[6].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  winnerResult=true;
              }
          }
          else  if(i==4)
          {
              if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                      gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                      gamestate[winningPosition[0]]!=2)
              {
                  buttons[1].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[4].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[7].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  winnerResult=true;
              }
          }else  if(i==5)
          {
              if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                      gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                      gamestate[winningPosition[0]]!=2)
              {
                  buttons[2].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[5].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[8].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  winnerResult=true;
              }
          }else  if(i==6)
          {
              if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                      gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                      gamestate[winningPosition[0]]!=2)
              {
                  buttons[0].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[4].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[8].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  winnerResult=true;
              }
          }else  if(i==7)
          {
              if (gamestate[winningPosition[0]]==gamestate[winningPosition[1]] &&
                      gamestate[winningPosition[1]]==gamestate[winningPosition[2]] &&
                      gamestate[winningPosition[0]]!=2)
              {
                  buttons[2].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[4].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  buttons[6].setBackgroundColor(Color.parseColor("#F5F5DC"));
                  winnerResult=true;
              }
          }
          else
          {
              winnerResult=false;
          }

           i++;
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
        if (checkwinner()==false)
        {

            numberofrounds = 0;
            activeplayer = true;


            AlertDialog.Builder win=new AlertDialog.Builder(MainActivity.this);
            win.setMessage("draw!");
            win.setCancelable(false);
            win.setPositiveButton("ok", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i=0;i<buttons.length;i++) {
                        gamestate[i] = 2;
                        buttons[i].setText("");
                        buttons[i].setBackgroundColor(Color.parseColor("#148F77"));
                    }
                }
            });
            AlertDialog alertDialog=win.create();
            alertDialog.show();

        }
        else {
            numberofrounds = 0;
            activeplayer = true;
            AlertDialog.Builder win = new AlertDialog.Builder(MainActivity.this);
            win.setMessage("congratulations!");
            win.setCancelable(false);
            win.setPositiveButton("thanks", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < buttons.length; i++) {
                        gamestate[i] = 2;
                        buttons[i].setText("");
                        buttons[i].setBackgroundColor(Color.parseColor("#148F77"));
                    }
                }
            });
            AlertDialog alertDialog = win.create();
            alertDialog.show();
        }
    }
}