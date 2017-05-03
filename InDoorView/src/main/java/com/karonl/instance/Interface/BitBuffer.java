package com.karonl.instance.Interface;

import android.graphics.Bitmap;

import com.karonl.instance.Adapter.BitAdapter;
import com.karonl.instance.Unit.PathUnit;

import java.util.List;

/**
 * Created by karonl on 16/4/1.
 * 为绘制canvas供应getBitBuffer和路径
 */
public interface BitBuffer {

    List<PathUnit> getPathUnit();

    Bitmap getBitBuffer();

    void setOnAdapterListener(BitAdapter.AttrListener listener);
}
