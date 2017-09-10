package io.keiji.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {
    TextView textView;
    Button startGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        textView = (TextView)findViewById(R.id.textView2);
        startGame = (Button)findViewById(R.id.button2);

    }
    public void startGame(View v) {

        Intent intent = new Intent(this, MainActivity.class);
       startActivity(intent);
    }
}
