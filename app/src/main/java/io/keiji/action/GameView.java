package io.keiji.action;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;



public class GameView extends SurfaceView implements SurfaceHolder.Callback {




    private static final float POWER_GAUGE_HEIGHT = 30;
    private static final Paint PAINT_POWER_GAUGE =  new Paint();

    static {
        PAINT_POWER_GAUGE.setColor(Color.RED);
    }

    private static final int GROUND_MOVE_TO_LEFT = 10;
    private static final int GROUND_HEIGHT = 50;

    private static final int ADD_GROUND_COUNT = 5;

    private static final int GROUND_WIDTH = 340;
    private static final int GROUND_BLOCK_HEIGHT = 100;

    private Ground lastGround;

    private final List<Ground>groundList = new ArrayList<>();
    private final Random rand = new Random(System.currentTimeMillis());

    private Bitmap droidBitmap;

    private int windowCount = 0;

    private Droid droid;


    private final Droid.Callback droidCallback = new Droid.Callback() {
        //Droid.Callbackインターフェイス（何かと何かのくっついている部分のこと）の処理をプログラムする





        @Override

        public int getDistanceFromGround(Droid droid) {//1

            int width = getWidth();
            int height = getHeight();

            for (Ground ground : groundList){
                if (!ground.isShown(width,height)){
                    continue;
                }


                boolean horizontal = !(droid.hitRect.left >= ground.rect.right||
                        droid.rect.right <= ground.rect.left);
                if (horizontal){
                    if (!ground.isSolid()){
                        return Integer.MAX_VALUE;
                    }

                    int distanceFromGround = ground.rect.top - droid.hitRect.bottom;
                    if (distanceFromGround < 0){
                        gameOver();
                        return Integer.MAX_VALUE;
                    }
                    return distanceFromGround;
                }
            }



            return Integer.MAX_VALUE;
        }
    };

           //2 自機が地面の上に存在するかを判定
           //3 自機の下に地面がなければ、地面との距離に最大値（integer.MAX_VALUE）を返すことで自機は落下し続ける
           //1 getDistanceFromGroundメソッドで地面との距離を計算
           //1  GroundメソッドはDroidの中から呼ばれてる



    private static final long DRAW_INTERVAL = 1000 / 100;

    private class DrawThread extends Thread {
        private final AtomicBoolean isFinished = new AtomicBoolean(false);

        public void finish() {
            isFinished.set(true);
        }

        @Override
        public void run() {
            SurfaceHolder holder = getHolder();

            while (!isFinished.get()) {
                if (holder.isCreating()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                if (canvas == null) {
                    continue;
                }

                drawGame(canvas);

                holder.unlockCanvasAndPost(canvas);
                synchronized (this) {
                    try {
                        wait(DRAW_INTERVAL);
                    } catch (InterruptedException e) {

                    }
                }
            }


        }
    }

    private DrawThread drawThread;

    public void startDrawThread(){
        stopDrawThread();

        drawThread = new DrawThread();
        drawThread.start();
    }

    public boolean stopDrawThread(){
        if(drawThread==null){
            return false;
        }
        drawThread.finish();
        drawThread = null;

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
    }

    @Override
    public void surfaceChanged(
            SurfaceHolder holder, int format,int width,int height) {
        startDrawThread();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }



    private final Handler handler = new Handler();

    public interface GameOverCallback {
        void onGameOver();
    }

    private GameOverCallback gameOverCallback;


    public void setCallback(GameOverCallback callback){
        gameOverCallback = callback;
    }

    private final AtomicBoolean isGameOver = new AtomicBoolean();



    private void gameOver(){
        if (isGameOver.get()){
            return;
        }
        isGameOver.set(true);
        droid.stop();
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameOverCallback.onGameOver();
            }
        });

    }

    public GameView(Context context) {

        super(context);

        droidBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.droid_twins);
        //droidの設計図（クラス）を具現化した「実体」（インスタンス）を作るときにdroid.pngのBitmapを渡す
        droid = new Droid(droidBitmap, 0, 0, droidCallback);
        //Droidを設計図（クラス）を具現化した「実体」（インスタンス）のこと化する際のコンストラクタ（クラスをnewした瞬間に実行される関数のこと）にdroidCallbackオブジェクトを渡す


        getHolder().addCallback(this);
    }



    private void drawGame(Canvas canvas){
        canvas.drawColor(Color.WHITE);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if (lastGround == null){
            int top = height - GROUND_HEIGHT;
            lastGround = new Ground(0,top,width,height);
            groundList.add(lastGround);
        }

        if (lastGround.isShown(width,height)){
            for (int i = 0; i < ADD_GROUND_COUNT;i++){
                int left = lastGround.rect.right;

                int groundHeight = rand.nextInt(height / GROUND_BLOCK_HEIGHT) *
                        GROUND_BLOCK_HEIGHT / 2 + GROUND_BLOCK_HEIGHT;
                System.out.println(i + "こ目のグランドです");

                if (i == 4){
                    windowCount++;
                    System.out.println(windowCount);
                }
                if (windowCount == 5){

                   Intent intent = new Intent(getContext(), GoalActivity
                           .class);
                    Context context = getContext();
                    context.startActivity(intent);

                }

                int top = height - groundHeight;
                int right = left + GROUND_WIDTH;

                if (i % 2 == 0){
                    lastGround = new Ground(left,top,right,height);
                }else {
                    lastGround = new Blank(left,top,right,height);
                }
                groundList.add(lastGround);
            }
        }


        for (int i = 0;i < groundList.size(); i++){
            Ground ground = groundList.get(i);

            if (ground.isAvailable()) {
                ground.move(GROUND_MOVE_TO_LEFT);
                if (ground.isShown(width, height)) {
                    ground.draw(canvas);
                }
            } else {
                groundList.remove(ground);
                i--;
            }
        }



        droid.move();
        //droidのdrawメソッドを実行する前に、moveの「操作」を定義したもののこと（メソッド）を実行
        droid.draw(canvas);

        if (touchDownStartTime > 0){
            float elapsedTime = System.currentTimeMillis() - touchDownStartTime;
            canvas.drawRect(0,0,width * (elapsedTime / MAX_TOUCH_TIME),
                    POWER_GAUGE_HEIGHT,PAINT_POWER_GAUGE);
        }


    }

    private static final long MAX_TOUCH_TIME = 500; //ミリ秒
    private long touchDownStartTime;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //画面（View）へのタッチは、onTouchEventメソッドで受け取る}
        //タッチされた場所などの具体的な内容は MotionEventのオブジェクトに含まれる
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://②タッチには、指が触れた時のACTION_DOENと
                //　　　　　　                           指が離れた時のACTION_UPのイベントがある
                touchDownStartTime = System.currentTimeMillis();
                //ACTION_DOWNのタイミングではイベントが発生した時間をフィールドtouchDownStartTimeに保持する
                //実際のジャンプ処理はACTION_UPのタイミングでjumpDroidメソッドを呼び出す
                return true;
            case MotionEvent.ACTION_UP:
                //②と同じ
                float time = System.currentTimeMillis() - touchDownStartTime;
                //jumpDroidメソッドでは、ACTION_DWONの時間とACTION_UPの時間から、指触れていた時間を計算する
                jumpDroid(time);
                touchDownStartTime = 0;
                break;
        }
        return super.onTouchEvent(event);
    }

    private void jumpDroid(float time) {
        //ここから
        if (droidCallback.getDistanceFromGround(droid) > 0) {
            return;
        }
        //jumpメソッドを実行した時点で、自機と地面との距離が離れている場合は、jumpメソッドは実行しない

        droid.jump(Math.min(time,MAX_TOUCH_TIME)/MAX_TOUCH_TIME);
        //timeとMAX_TOUCH_TIMEで小さいほうの値を指が触れていた時間とする
        //（MAX_TOUCH_TIMEを上限とする）
        //時間の上限と指が触れていた時間の比をjumpのパワーとしてDroidのjumpメソッドにわたす
}
//ゲームのことについてのプログラム
    }
