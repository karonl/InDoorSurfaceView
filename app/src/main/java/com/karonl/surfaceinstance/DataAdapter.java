package com.karonl.surfaceinstance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;

import com.karonl.surfaceinstance.Interface.BitBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karonl on 16/4/1.
 * 处理和填充数据
 */
public class DataAdapter extends BitAdapter {

    private List<PathUnit> list;

    public DataAdapter(List<PathUnit> list, Bitmap bmp){
        drawBackground(bmp);//绘制背景
        this.list = list;
        drawBuffer(this);
    }

    public void refreshData(){
        drawBuffer(this);
    }

    @Override
    public List<PathUnit> getPathUnit() {
        if(list == null)
            return new ArrayList<>();
        else
            return list;
    }
}
