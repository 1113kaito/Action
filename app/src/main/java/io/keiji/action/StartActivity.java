package io.keiji.action;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity{
    TextView textView;
    Button start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        textView = (TextView)findViewById(R.id.textView2);
        start = (Button)findViewById(R.id.button2);


    }
    public void start(View v){

      Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }



}
