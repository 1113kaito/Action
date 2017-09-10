package io.keiji.action;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Ground {


    private int COLOR = Color.rgb(153,76,0);//茶色
    private int COLORGreen = Color.rgb(0,128,0);
    private Paint paint = new Paint();
    private Paint paintGreen = new Paint();

    final Rect rect;
    final Rect rectGreen;
    public Ground(int left,int top,int right,int bottom){
        rect = new Rect(left,top,right,bottom);
        rectGreen = new Rect(left,top +20,right,top);

        paint.setColor(COLOR);
        paintGreen.setColor(COLORGreen);
    }
    //groundの設計図（クラス）を具現化した「実体」（インスタンス）はlef(左側),top(上側),right(右側),bottom(下側)の4つをオブジェととして保存する

    public void draw(Canvas canvas){
        canvas.drawRect(rect,paint);
        //drawメソッドはRectオブジェクトに茶色の四角を表示
        canvas.drawRect(rectGreen,paintGreen);
    }

    public void move (int moveToLeft){

        rect.offset(-moveToLeft,0);
        rectGreen.offset(-moveToLeft, 0);
        //引数moveToLetの分だけ横方向に移動する
    }

    public boolean isShown(int width,int height){
        if (rect.intersects(0,0,width,height) || rectGreen.intersects(0,0,width,height)){
            return true;
        } else {
            return false;
        }
    }
    public boolean isAvailable(){
        return rect.right > 0;
    }



    public boolean isSolid(){
        return true;
    }
    //地面についてのプログラム
}
