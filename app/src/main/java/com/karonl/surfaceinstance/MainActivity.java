package com.karonl.surfaceinstance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.karonl.surfaceinstance.Adapter.DataAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textview;
    private DataAdapter adapter;
    List<PathUnit> unitList = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            float fr = (float)msg.obj;
            int fps = (int)(1000f / fr);
            textview.setText("FPS: "+fps+"");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.surface);

        //背景图
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        final Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.zxc, opt);//图片资源
        //数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){}
                getUnitList();
                adapter.refreshData();
            }
        }).start();
        adapter = new DataAdapter(unitList, bmp);

        textview = (TextView)findViewById(R.id.frames);

        InDoorSurfaceView view = new InDoorSurfaceView(this,null);
        view.init(adapter);//初始化
        view.onFramesListener(new InDoorSurfaceView.FramesListener() {
            @Override
            public void onRefresh(float number) {
                Message message = handler.obtainMessage();
                message.obj = number;
                message.sendToTarget();
            }
        });
        frameLayout.addView(view);

    }


    //图案列表
    private void getUnitList(){
        PathUnit unit = new PathUnit(getList());
        unit.setName("广州文琪信息科技");
        unitList.add(unit);
    }

    //每个图案的坐标组合
    private List<PointF> getList(){
        float density = getResources().getDisplayMetrics().density;
        List<PointF> pointList = new ArrayList<>();
        pointList.add(new PointF(99.1f * density,673.1f * density));
        pointList.add(new PointF(222.1f * density,670.1f * density));
        pointList.add(new PointF(227.1f * density,327.1f * density));
        pointList.add(new PointF(94.1f * density,321.1f * density));
        pointList.add(new PointF(100.1f * density,674.1f * density));

        return pointList;
    }



}
