package com.karonl.instance;

import android.app.Activity;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.SystemClock;
import android.view.MotionEvent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

/**
 * Created by karonl on 16/4/12.
 * 测试加载并且拖动
 */

@RunWith(RobolectricGradleTestRunner.class)//这里要注意
@Config(constants = BuildConfig.class, sdk = 18, manifest = "src/main/AndroidManifest.xml")//这里要写全,field 'constants' not specified in @config annotation
public class MainTestActivity {
    @Test
    public void testSurface(){
//        Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();
//        InDoorView view = (InDoorView) activity.findViewById(R.id.surface);
//        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis()+10, SystemClock.uptimeMillis()+12, MotionEvent.ACTION_DOWN, view.getLeft()+5, view.getTop()+5, 0));
//        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis()+30, SystemClock.uptimeMillis()+32, MotionEvent.ACTION_MOVE, view.getLeft()+65, view.getTop()+65, 0));
//        List<ShadowLog.LogItem> aa = ShadowLog.getLogs();
//        Assert.assertEquals(aa.get(aa.size()-1), new ShadowLog.LogItem(4,"move","move",null));
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(0.0f, 3.0f);
        path.lineTo(3.0f, 3.0f);
        path.lineTo(3.0f, 0.0f);

        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));

        System.out.println(region.contains(2,2));
    }
}