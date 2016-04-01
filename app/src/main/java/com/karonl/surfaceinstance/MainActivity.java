package com.karonl.surfaceinstance;

import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private InDoorSurfaceView view;
    private TextView textview;
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

        textview = (TextView)findViewById(R.id.frames);
        view = (InDoorSurfaceView)findViewById(R.id.surface);
        view.init(null, getList());//初始化
        view.onFramesListener(new InDoorSurfaceView.FramesListener() {
            @Override
            public void onRefresh(float number) {
                Message message = handler.obtainMessage();
                message.obj = number;
                message.sendToTarget();
            }
        });
    }

    private List<PointF> getList(){
        float density = this.getResources().getDisplayMetrics().density;

        List<PointF> pointList = new ArrayList<>();
        pointList.add(new PointF(99.1f * density,673.1f * density));
        pointList.add(new PointF(222.1f * density,670.1f * density));
        pointList.add(new PointF(227.1f * density,327.1f * density));
        pointList.add(new PointF(94.1f * density,321.1f * density));
        pointList.add(new PointF(100.1f * density,674.1f * density));

        return pointList;
    }

}
