package com.karonl.instance.Adapter;

import android.graphics.Bitmap;

import com.karonl.instance.Unit.PathUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karonl on 16/4/1.
 * 处理和填充数据drawBitmap,drawBuffer是BitAdapter的方法
 */
public class DataAdapter extends BitAdapter {

    private List<PathUnit> list;
    private Bitmap bmp;

    public DataAdapter() {

    }

    public DataAdapter(List<PathUnit> list) {
        this.list = list;
        drawBitmap(this);
        drawBuffer(this);
    }

    public DataAdapter(List<PathUnit> list, Bitmap bmp) {
        this.bmp = bmp;
        this.list = list;
        drawBitmap(this);
        drawBuffer(this);
    }

    public void setList(List<PathUnit> list) {
        this.list = list;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = null;
        this.bmp = bmp;
    }

    public void refreshData() {
        drawBitmap(this);
        drawBuffer(this);
        if (!bmp.isRecycled()) {
            bmp.recycle();
        }
        this.bmp = null;
    }

    @Override
    public Bitmap getBgBitmap() {
        return this.bmp;
    }

    @Override
    public List<PathUnit> getPathUnit() {
        if (list == null)
            return new ArrayList<>();
        else
            return list;
    }
}
