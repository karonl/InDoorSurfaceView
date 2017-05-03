package com.karonl.instance.Unit;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;

import java.util.List;

/**
 * Created by karonl on 16/3/27.
 * 集合了坐标点和判断Region
 */
public class PathUnit {

    public String name;
    public String id;
    public Region region;
    public Path path;

    public PathUnit(List<PointF> list) {
        int i = 0;
        path = new Path();
        for (PointF point : list) {
            if (i == 0) path.moveTo(point.x, point.y);
            path.lineTo(point.x, point.y);
            i++;
        }
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}
