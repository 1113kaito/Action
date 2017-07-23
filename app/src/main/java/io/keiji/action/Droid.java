package io.keiji.action;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

//ここから
public class Droid {

    private static final int BLOCK_SIZE = 153;

    private static final Rect BITMAP_SRC_JUMPING = new Rect(
            BLOCK_SIZE,0,BLOCK_SIZE * 2,BLOCK_SIZE);
    private static final Rect BITMAP_SRC_RUNNING = new Rect(
            0,0,BLOCK_SIZE, BLOCK_SIZE);

    private static final int HIT_MARGIN_LEFT = 30;
    private static final int HIT_MARGIN_RIGHT = 10;


    private static final float GRAVITY = 0.8f;
    private static final float WEIGHT = GRAVITY * 60;

    private final Paint paint = new Paint();

    private Bitmap bitmap;

    final RectF rect;

    final Rect hitRect;


    public interface Callback {
        int getDistanceFromGround(Droid droid);
    }
    //Callbackの何かと何かのくっついている部分のことを追加（インターフェイス）。Callbackのくっついている部分はDroidと地面と距離を取得できる





    private final Callback callback;


    public Droid(Bitmap bitmap, int left, int top, Callback callback){
        this.bitmap = bitmap;
        int right = left + BLOCK_SIZE;
        int bottom = top + bitmap.getHeight();
        this.rect = new RectF(left,top,right,bottom);
        this.hitRect = new Rect(left,top,right,bottom);
        this.hitRect.left += HIT_MARGIN_LEFT;
        this.hitRect.right -= HIT_MARGIN_RIGHT;
        //GameViewにすでにある自機を表示させる処理をここに移動
        // 新しいコメント
        //新しいコメント②
        this.callback = callback;
    }
    public void draw(Canvas canvas){
        Rect src = BITMAP_SRC_RUNNING;
        //通常　->running
        if (velocity != 0){
            src = BITMAP_SRC_JUMPING;
            //ジャンプ中or落下中->jumping
        }
        canvas.drawBitmap(bitmap, src, rect, paint);



    }

    private float velocity = 0;

    //ここから
    public void jump(float power){
        velocity = (power * WEIGHT);
    }
//jumpメソッド（「操作」を定義したもののこと）で引数powerと定数WEIGHTを元にvelocity（速度）を設定する

    public void stop(){
        velocity = 0;
    }

    public void move(){
        int distanceFromGround = callback.getDistanceFromGround(this);
        //ここから
        if (velocity < 0&& velocity < -distanceFromGround){
            velocity = -distanceFromGround;
        }//地面との距離＜落下速度は、自機が地面を突き抜けてしまうので、地面との距離だけ移動する様に調整する

        rect.offset(0,Math.round(-1 * velocity));
        //moveメソッドで調整済みの速度分、縦方向に移動する。０より大きい場合は上向きの力、
        // 　　　　　　　　　　　　　　　　　　　　　　　　　０より小さい場合は下向きの力
        hitRect.offset(0,Math.round(-1 * velocity));


        if (distanceFromGround == 0){
            //距離が０なら落下を止める
            return;
        }else if (distanceFromGround < 0){
            //地面との距離が０未満だったら０になる様に位置を補正する様にする
            rect.offset(0,distanceFromGround);
            hitRect.offset(0,distanceFromGround);
            return;
        }


     velocity -= GRAVITY;
        //現在の速度から定数GRAVITYを減算する。velocityがマイナス値になると自機の移動が上昇から下降に変わる

    }
    //ドロイド君についてのプログラム
}
