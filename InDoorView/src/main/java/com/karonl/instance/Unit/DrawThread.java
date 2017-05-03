package com.karonl.instance.Unit;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.karonl.instance.Interface.DrawFramesListener;
import com.karonl.instance.Interface.FramesListener;

/**
 * Created by karonl on 2017/5/3.
 * 绘制线程
 */

public class DrawThread extends Thread {
    private boolean _run = true;
    private boolean canPaint = true; // 是否直接暂停
    private int FRAME_INTERVAL = 10;// 默认帧时间10ms
    private DrawFramesListener listener;
    private FramesListener listener2;

    private final SurfaceHolder surfaceHolder;
    private Canvas c = null;

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void onDrawingListener(DrawFramesListener listener) {
        this.listener = listener;
    }

    public void onFramesListener(FramesListener listener2) {
        this.listener2 = listener2;
    }

    @Override
    public void run() {
        while (_run) {
            showBit();//绘制
        }
    }

    /**
     * 绘制图画
     */
    private void showBit() {
        if (canPaint && surfaceHolder != null) { // fps: !stop
            long startTime = System.currentTimeMillis();
            c = surfaceHolder.lockCanvas(); // 注意lock的时间消耗
            Log.e("lock", System.currentTimeMillis() - startTime + "");
            try {
                synchronized (surfaceHolder) {
                    // 调用外部接口
                    this.listener.onDraw(c);
                    // 调用外部接口
                    long endTime = System.currentTimeMillis();
                    /**
                     * 计算出绘画一次更新的毫秒数
                     * **/
                    int diffTime = (int) (endTime - startTime);

                    if (diffTime < FRAME_INTERVAL) {
                        try {
                            Thread.sleep(FRAME_INTERVAL - diffTime);
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                    }
                    //发送一次循环运行总时间
                    if (listener2 != null)
                        listener2.onRefresh(System.currentTimeMillis() - startTime);
                }

            } finally {
                surfaceHolder.unlockCanvasAndPost(c);
            }
        } else { // fps: stop
            try {
                Thread.sleep(FRAME_INTERVAL);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            if (listener2 != null) listener2.onRefresh(1000f);
        }
    }

    public void setThreadRun(boolean run) { // 设置是否暂停
        this._run = run;
    }

    public void setCanPaint(boolean can) { // 设置是否绘制
        this.canPaint = can;
    }

}
