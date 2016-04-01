package com.karonl.surfaceinstance.Adapter;

import android.graphics.Bitmap;

import com.karonl.surfaceinstance.PathUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karonl on 16/4/1.
 * 处理和填充数据
 */
public class DataAdapter extends BitAdapter {

    private List<PathUnit> list;

    public DataAdapter(List<PathUnit> list, Bitmap bmp){
        super(bmp);//绘制背景
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
