package com.karonl.surfaceinstance;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.karonl.surfaceinstance.Interface.BitBuffer;

import java.util.List;

/**
 * Created by karonl on 16/4/1.
 * 负责向view输出bitmap缓存图片
 */
public abstract class BitAdapter implements BitBuffer {

    Bitmap bitmap = null;
    Bitmap bg = null;
    Canvas bufferCanvas = null;
    List<PathUnit> pathUnitList = null;

    public void drawBackground(Bitmap bg){
        if(bg == null)
            this.bg = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        else
            this.bg = bg;
    }

    public void drawBuffer(BitAdapter child) {
        //根据底图申请缓冲区
        bitmap = Bitmap.createBitmap(bg.getWidth(),bg.getHeight(), Bitmap.Config.RGB_565);//创建内存位图
        //创建空白绘图画布
        bufferCanvas = new Canvas(bitmap);
        //底图进来后绘制到缓冲区
        bufferCanvas.drawBitmap(bg, new Rect(0, 0, bg.getWidth(), bg.getHeight()), new Rect(0, 0, bg.getWidth(), bg.getHeight()), null);
        //填充数据
        pathUnitList = child.getPathUnit();
        //画图案
        for (PathUnit path : pathUnitList) {
            bufferCanvas.drawPath(path.path, getPaint());
        }
    }

    public abstract List<PathUnit> getPathUnit();
    private float scale;


    @Override
    public Bitmap getBitBuffer() {
        return bitmap;
    }

    @Override
    public float getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public float getWidth() {
        return bitmap.getWidth();
    }

    //获得画笔
    private Paint paint;//画笔属性
    private Paint getPaint(){
        if(paint == null) {
            paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(30);
        }
        return paint;
    }
}
