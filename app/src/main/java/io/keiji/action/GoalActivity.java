package io.keiji.action;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GoalActivity extends AppCompatActivity{

    TextView textView;
    Button start;
    Button last;
    MediaPlayer mp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        textView = (TextView) findViewById(R.id.textView);
        start = (Button)findViewById(R.id.button);
        last = (Button)findViewById(R.id.button2);
        mp = MediaPlayer.create(this,R.raw.goal);
        mp.start();

    }
    public void start(View v){
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
        mp.stop();

    }


    public void play(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        mp.stop();

    }
}

