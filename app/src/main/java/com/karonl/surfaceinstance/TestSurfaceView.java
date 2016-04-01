package com.karonl.surfaceinstance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by karonl on 16/3/21.
 */

public class TestSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, View.OnTouchListener {

    private static final int NONE = 0;// 原始
    private static final int DRAG = 1;// 拖动
    private static final int ZOOM = 2;// 放大
    private int mStatus = NONE;

    private static final float MAX_ZOOM_SCALE = 4.0f;
    private static final float MIN_ZOOM_SCALE = 1.0f;
    private static final float FLOAT_TYPE = 1.0f;
    private float mCurrentMaxScale = MAX_ZOOM_SCALE;
    private float mCurrentScale = 1.0f;

    private Rect mRectSrc = new Rect(); // used for render image.
    private Rect mRectDes = new Rect(); // used for store size of monitor.

    private int mCenterX, mCenterY;
    int mSurfaceHeight, mSurfaceWidth, mImageHeight, mImageWidth;

    private PointF mStartPoint = new PointF();
    private float mStartDistance = 0f;

    private SurfaceHolder mSurHolder = null;
    private Bitmap mBitmap;

    public TestSurfaceView(Context context, AttributeSet attributeSet) {
        super(context);
        mSurHolder = getHolder();
        mSurHolder.addCallback(this);
        this.setOnTouchListener(this);

    }


    private void init() {
        mCurrentMaxScale = Math.max(
                MIN_ZOOM_SCALE,
                4 * Math.min(FLOAT_TYPE * mImageHeight / mSurfaceHeight, 1.0f
                        * mImageWidth / mSurfaceWidth));
        mCurrentScale = MIN_ZOOM_SCALE;
        mCenterX = mImageWidth / 2;
        mCenterY = mImageHeight / 2;
        calcRect();

    }

    private void adjustCenter() {
        int w = mRectSrc.right - mRectSrc.left;
        int h = mRectSrc.bottom - mRectSrc.top;

        if (mCenterX - w / 2 < 0) {
            mCenterX = w / 2;
            mRectSrc.left = 0;
            mRectSrc.right = w;
        } else if (mCenterX + w / 2 >= mImageWidth) {
            mCenterX = mImageWidth - w / 2;
            mRectSrc.right = mImageWidth;
            mRectSrc.left = mRectSrc.right - w;

        } else {
            mRectSrc.left = mCenterX - w / 2;
            mRectSrc.right = mRectSrc.left + w;
        }

        if (mCenterY - h / 2 < 0) {
            mCenterY = h / 2;
            mRectSrc.top = 0;
            mRectSrc.bottom = h;
        } else if (mCenterY + h / 2 >= mImageHeight) {
            mCenterY = mImageHeight - h / 2;
            mRectSrc.bottom = mImageHeight;
            mRectSrc.top = mRectSrc.bottom - h;
        } else {
            mRectSrc.top = mCenterY - h / 2;
            mRectSrc.bottom = mRectSrc.top + h;
        }

    }

    private void calcRect() {
        int w, h;
        float imageRatio, surfaceRatio;
        imageRatio = FLOAT_TYPE * mImageWidth / mImageHeight;
        surfaceRatio = FLOAT_TYPE * mSurfaceWidth / mSurfaceHeight;

        if (imageRatio < surfaceRatio) {
            h = mSurfaceHeight;
            w = (int) (h * imageRatio);
        } else {
            w = mSurfaceWidth;
            h = (int) (w / imageRatio);
        }

        if (mCurrentScale > MIN_ZOOM_SCALE) {
            w = Math.min(mSurfaceWidth, (int) (w * mCurrentScale));
            h = Math.min(mSurfaceHeight, (int) (h * mCurrentScale));
        } else {
            mCurrentScale = MIN_ZOOM_SCALE;
        }

        mRectDes.left = (mSurfaceWidth - w) / 2;
        mRectDes.top = (mSurfaceHeight - h) / 2;
        mRectDes.right = mRectDes.left + w;
        mRectDes.bottom = mRectDes.top + h;

        float curImageRatio = FLOAT_TYPE * w / h;
        int h2, w2;
        if (curImageRatio > imageRatio) {
            h2 = (int) (mImageHeight / mCurrentScale);
            w2 = (int) (h2 * curImageRatio);
        } else {

            w2 = (int) (mImageWidth / mCurrentScale);
            h2 = (int) (w2 / curImageRatio);
        }
        mRectSrc.left = mCenterX - w2 / 2;
        mRectSrc.top = mCenterY - h2 / 2;
        mRectSrc.right = mRectSrc.left + w2;
        mRectSrc.bottom = mRectSrc.top + h2;
    }

    public void setMaxZoom(float value) {
        mCurrentMaxScale = value;
    }

    public void setBitmap(Bitmap b) {

        if (b == null) {
            return;
        }
        synchronized (TestSurfaceView.class) {
            mBitmap = b;
            if (mImageHeight != mBitmap.getHeight() || mImageWidth != mBitmap.getWidth()) {
                mImageHeight = mBitmap.getHeight();
                mImageWidth = mBitmap.getWidth();
                init();
            }
            showBitmap();
        }

    }

    private void showBitmap() {
        synchronized (TestSurfaceView.class) {
            Canvas c = getHolder().lockCanvas();
            if (c != null && mBitmap != null) {
                c.drawColor(Color.GRAY);
                c.drawBitmap(mBitmap, mRectSrc, mRectDes, null);
                getHolder().unlockCanvasAndPost(c);
            }
        }
    }

    private void showBitmap(float x, float y) {

        RectF rectF = new RectF();
        Path path = new Path();
        path.moveTo(0,0);
        path.lineTo(100,100);
        path.lineTo(100,0);
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        if (region.contains((int) x, (int) y)) {
            Log.d(this.getClass().getName(),"click");
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        //synchronized (TestSurfaceView.class) {
            Canvas c = getHolder().lockCanvas();
            if (c != null && mBitmap != null) {


                c.drawColor(Color.GRAY);
                c.translate(x,y);
                //图片的图案上的四个点范围内要显示
                //这个图案的绘画在画布的四个位置点
                c.drawBitmap(mBitmap, new Rect(0, 0, 200, 200) ,new Rect(0, 0, 1000, 1000), null);
                c.drawPath(path, paint);
                getHolder().unlockCanvasAndPost(c);
            }
        //}
    }

    private void dragAction(MotionEvent event) {


        synchronized (TestSurfaceView.class) {
            PointF currentPoint = new PointF();
            currentPoint.set(event.getX(), event.getY());
            int offsetX = (int) currentPoint.x - (int) mStartPoint.x;
            int offsetY = (int) currentPoint.y - (int) mStartPoint.y;
            mStartPoint = currentPoint;

            mCenterX -= offsetX;
            mCenterY -= offsetY;

            System.out.println("x"+event.getX());

            adjustCenter();
            showBitmap(event.getX(), event.getY());
        }
    }

    private void zoomAcition(MotionEvent event) {

        synchronized (TestSurfaceView.class) {

            float newDist = spacing(event);
            float scale = newDist / mStartDistance;
            mStartDistance = newDist;

            Log.i(""+this.getClass().getName(),"test"+mStartDistance);

            mCurrentScale *= scale;
            mCurrentScale = Math.max(FLOAT_TYPE, Math.min(mCurrentScale, mCurrentMaxScale));

            calcRect();
            adjustCenter();
            showBitmap();
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
                float distance = spacing(event);
                if (distance > 10f) {

                    mStatus = ZOOM;
                    mStartDistance = distance;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (mStatus == DRAG) {
                    dragAction(event);
                } else {

                    if (event.getPointerCount() == 1)
                        return true;
                    zoomAcition(event);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mStatus = NONE;
                break;
            default:
                break;
        }

        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    // 初始化
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        synchronized (TestSurfaceView.class) {
            mRectDes.set(0, 0, width, height);
            mSurfaceHeight = height;
            mSurfaceWidth = width;
            init();
            if (mBitmap != null) {
                showBitmap();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}