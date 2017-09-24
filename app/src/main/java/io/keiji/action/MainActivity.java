package io.keiji.action;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements GameView.GameOverCallback{
    Button button;


    private GameView gameView;

    private MediaPlayer mp;


    @Override
    public void onGameOver(){
        Intent intent = new Intent(this,LastActivity.class);
        startActivity(intent);
        if (mp.isPlaying()) {
            mp.stop();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mp = MediaPlayer.create(this,R.raw.maoudamashii_shiro01);
        gameView = new GameView(this);
        //GameVieを作成している
        gameView.setCallback(this);
        setContentView(gameView);
        //setContentViewに指定したレイアウトファイルの参照をGameViewの設計図（クラス）を具現化した「実体」に変更
        mp.start();
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mp.isPlaying()){
            mp.stop();
        }
    }
}


