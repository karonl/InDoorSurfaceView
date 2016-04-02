package com.karonl.surfaceinstance;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.karonl.surfaceinstance.Interface.BitBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by karonl on 16/3/21.
 *
 */
public class InDoorSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {


    private static final int DRAG = 1;// 拖动
    private static final int ZOOM = 2;// 放大
    private int mStatus = 0;//状态
    private float mStartDistance; //初始距离
    private float mPicWidth, mPicHeight; //图案宽度,图案高度,实时状态
    private float screenWidth, screenHeight;
    private PointF mStartPoint = new PointF(); //下手点
    private float scale; //放大倍数
    private float bx ,by; //图案初始坐标
    private Canvas c = null;
    private Thread drawThread;//绘制线程
    private final SurfaceHolder surfaceHolder;
    private BitBuffer adapter;

    public InDoorSurfaceView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setOnTouchListener(this);
        getHolder().addCallback(this);
        surfaceHolder = getHolder();
    }

    public void init(BitBuffer adapter) {
        this.adapter = adapter;
    }

    private void setPicInit(){
        if(bx != 0 && by != 0)return;
        //图片初始状态
        mPicWidth = adapter.getWidth() * getScale(true);
        mPicHeight = adapter.getHeight() * getScale(true);
        //初始坐标
        bx = (screenWidth - mPicWidth)/2;
        by = (screenHeight - mPicHeight)/2;
    }

    private FramesListener listener;

    interface FramesListener{
        void onRefresh(float number);
    }

    public void onFramesListener(FramesListener listener){
        this.listener = listener;
    }

    private void looperRun(){
        scale = getScale(false);//实时的放大倍数
        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (_run){
                    showBit();//绘制
                }
            }
        });
        drawThread.start();
    }

    //获得比例,实时比例
    private float getScale(boolean original){
        if(scale != 0 && !original)return scale;
        float scaleWidth = screenWidth / adapter.getWidth();
        float scaleHeight = screenHeight / adapter.getHeight();
        float scale = scaleWidth>scaleHeight ? scaleHeight:scaleWidth;
        return scale;
    }
    /*
    *
    * 绘制图画
    *
    * */
    private void showBit(){
        long startTime = System.currentTimeMillis();
        synchronized (surfaceHolder) {
            c = surfaceHolder.lockCanvas();
            if(c!=null) {
                c.drawColor(Color.GRAY);
                c.translate(bx, by);
                c.scale(scale, scale);
                c.drawBitmap(adapter.getBitBuffer(), 0, 0, new Paint());
            }
            try {
                surfaceHolder.unlockCanvasAndPost(c);
            }catch (IllegalStateException e){ //已经释放
                Log.e("error:",""+e);
            }
        }
        long endTime = System.currentTimeMillis();

        /**计算出绘画一次更新的毫秒数**/
        int diffTime  = (int)(endTime - startTime);
        if(diffTime < 16) {
            try {
                Thread.sleep(16 - diffTime);
                //Log.d("diffTime",diffTime+"");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sendToInterface(System.currentTimeMillis() - startTime);//发送一次循环运行总时间
    }
    //显示帧数
    private boolean sendAble = true;
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    private void sendToInterface(final float diffTime){
        if(listener!=null && sendAble){
            sendAble = false;
            scheduledThreadPool.schedule(new Runnable() {
                @Override
                public void run() {
                    listener.onRefresh(diffTime);
                    sendAble = true;
                }
            }, 500, TimeUnit.MILLISECONDS);
        }
    }

    /*
    *
    * 手势事件
    *
    * */
    private void zoomMap(MotionEvent event){
        synchronized (InDoorSurfaceView.class) {
            float newDist = spacing(event);
            float scale1 = newDist / mStartDistance;
            mStartDistance = newDist;
            float tmp = scale * scale1;//缩放了比例值
            if(tmp < getScale(true) * 3 && tmp > getScale(true) * 0.6) {//放大的倍数范围
                scale = tmp;
            } else {
                return;
            }
            mPicHeight *= scale1;//缩放了高宽
            mPicWidth *= scale1;
            float fx = (event.getX(1) - event.getX(0))/2 + event.getX(0);//中点坐标
            float fy = (event.getY(1) - event.getY(0))/2 + event.getY(0);
            float XIn = fx - bx;//获得中点在图中的坐标
            float YIn = fy - by;
            XIn *= scale1;//坐标根据图片缩放而变化
            YIn *= scale1;
            bx = fx - XIn;//左上角的坐标等于中点坐标加图中偏移的坐标
            by = fy - YIn;
            //showBit(bx, by);
        }
    }

    private void drawMap(MotionEvent event){
        synchronized (InDoorSurfaceView.class) {
            PointF currentPoint = new PointF();
            currentPoint.set(event.getX(), event.getY());
            int offsetX = (int) (currentPoint.x - mStartPoint.x);
            int offsetY = (int) (currentPoint.y - mStartPoint.y);
            mStartPoint = currentPoint;
            bx += offsetX;
            by += offsetY;
            //showBit(bx ,by);
        }
    }

    public void setOnClickMapListener(onClickMapListener maplistener){
        this.maplistener = maplistener;
    }

    interface onClickMapListener{
        void onClick(PathUnit unit);
    }
    private onClickMapListener maplistener;
    private void clickMap(MotionEvent event){
        for(PathUnit region: adapter.getPathUnit()){
            if (region.region.contains((int) ((event.getX() - bx) /scale), (int) ((event.getY() - by) /scale) )) {
                if(listener!=null)maplistener.onClick(region);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mStartPoint.set(event.getX(), event.getY());
                mStatus = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                float distance = spacing(event); //初始距离
                if (distance > 5f) {
                    mStatus = ZOOM;
                    mStartDistance = distance;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStatus == DRAG) {
                    drawMap(event);
                } else {
                    if (event.getPointerCount() == 1)
                        return true;
                    zoomMap(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                clickMap(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            default:
                break;
        }
        return true;
    }

    // 初始化
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("x","surfaceCreated");
        screenWidth = this.getWidth();
        screenHeight = this.getHeight();
        setPicInit();
        looperRun();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {Log.e("x","surfaceChanged");}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("x","surfaceDestroyed");
        stopThread(true);
        drawThread.interrupt();
        drawThread = null;
    }

    //获取距离运算
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //控制绘画线程
    private boolean  _run  = true;
    public void stopThread(boolean  run) {
        this._run = !run;
    }
}
