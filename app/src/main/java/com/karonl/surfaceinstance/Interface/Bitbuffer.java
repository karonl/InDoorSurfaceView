package com.karonl.surfaceinstance.Interface;

import android.graphics.Bitmap;

import com.karonl.surfaceinstance.Adapter.BitAdapter;
import com.karonl.surfaceinstance.Unit.PathUnit;

import java.util.List;

/**
 * Created by karonl on 16/4/1.
 */
public interface BitBuffer {

    List<PathUnit> getPathUnit();
    Bitmap getBitBuffer();
    void setOnAdapterListener(BitAdapter.AttrListener listener);
}
