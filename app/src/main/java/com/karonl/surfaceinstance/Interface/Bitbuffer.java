package com.karonl.surfaceinstance.Interface;

import android.graphics.Bitmap;

import com.karonl.surfaceinstance.PathUnit;

import java.util.List;

/**
 * Created by karonl on 16/4/1.
 */
public interface BitBuffer {

    List<PathUnit> getPathUnit();
    Bitmap getBitBuffer();
    float getWidth();
    float getHeight();
}
