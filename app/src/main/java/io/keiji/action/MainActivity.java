package io.keiji.action;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements GameView.GameOverCallback{

    private GameView gameView;



    @Override
    public void onGameOver(){
        Toast.makeText(this,"Game Over",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        //GameVieを作成している
        gameView.setCallback(this);
        setContentView(gameView);
        //setContentViewに指定したレイアウトファイルの参照をGameViewの設計図（クラス）を具現化した「実体」に変更
    }
}
