# InDoorView [![Build Status](https://travis-ci.org/karonl/InDoorSurfaceView.svg?branch=master)](https://travis-ci.org/karonl/InDoorSurfaceView)

## 为何开发 InDoorView？

说说这个想法的由来，之前的一个 case ，大概是开发一个可以室内选工位的 app ，点击某个座位付费就是你账号名下的。这我们很容易想到是操作整齐得像表格的座位。可事实有点不一样，因为每个区域都是不同大小并且位置不一，而我们手上的材料只有一张标识有区域的 jpg 地图。

一开始我使用游戏引擎来做，结果可想而知，为了个简单功能引入整个库非常不明智。所以我开始着手开发一个可以精巧的操作图里不规则区域的库。

## 应用场景:

该控件可让室内图片上的区域拥有点击事件，可用于**开发电影院选座、商场购物地图、展位摊位在线预定、办公场地租赁工位**等需要操作不规则区域的功能。

MP4视频Demo: http://7xjzrl.com1.z0.glb.clouddn.com/Screenrecord20170504.mp4
![图片预览](https://leanclub.cn/741690-01c611a26b251661.png)      


## 原理:

把读取地图底图 bitmap 和使用 Paint 的钢笔路径集合一同绘制到一个 canvas 上保存，并通过继承 SurfaceView 把 canvas 绘制到双缓画布中，通过 canvas.drawBitmap 实现缩放和移动，重写 view 点击事件结合 Region 判断点击坐标位于哪个区域内，再通过接口反馈事件。

采用把所有图案内容事先缓存到 canvas 的方法，使用非 UI 线程进行绘制，可实现每秒 60 次左右的界面绘制，实现流畅的移动和缩放操作；在没交互情况下，暂停绘制及刷新以节约计算资源。

## 特性:

1. 性能较强，绘制达到 60 帧上下
2. 支持缩放以及拖动
3. 直接使用原生 SurfaceView ，无需导入庞大的引擎库


## Github:

https://github.com/karonl/InDoorSurfaceView (thanks for your star✨)

## Demo:

直接使用 Android Studio 导入工程即可运行

## 如何导入 InDoorView 库:

1. clone 到本地
2. 复制 InDoorView 文件夹到目标项目的根目录 (InDoorView 使用的是 apply plugin: 'com.android.library')
3. 在 settings.gradle 文件中 include ':InDoorView'
4. 在主模块(一般是 app 文件夹)中的 build.gradle 中添加依赖

```json
dependencies {
   compile project(':InDoorView')
}
```

5. 点击 Sync 进行构建
6. 根目录的 src 是 demo ，可做参考
**注:直接导入工程是 demo ，可直接运行测试**

## 如何使用:

初始化 在 xml 文件中使用进行声明
```xml
<com.karonl.instance.InDoorView
    android:id="@+id/surface"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
在对应的 activity 中进行引用，并通过设置适配器，把底图和区域 list 填入，并 refreshData();
```java
InDoorSurfaceView view = (InDoorSurfaceView)findViewById(R.id.surface);
DataAdapter adapter =  new DataAdapter();
view.setAdapter(adapter);//初始化
adapter.setBmp(bmp);//设置图片(底图)
adapter.setList(list);//设置数组(图上的可点区域)
adapter.refreshData();
```
上面代码的 list 的具体设置看这里:

(输入钢笔路径相对于图片左上角的节点坐标)
```java
//每个图案的节点坐标集合
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

//区域列表
private void getUnitList(){
    PathUnit unit = new PathUnit(getList());//把节点换成一个 PathUnit 元素
    unit.setName("John Market");//设置元素的名字
    unitList.add(unit);//添加到区域列表
}
```
**注：从资源读取的图片对应的坐标要乘上 desity ，网络加载的图片则不用**

## 接口说明: 

通过该接口可以返回点击到的区域的 PathUnit 元素，可通过此来获取区域名字等信息
```java
view.setOnClickMapListener(new InDoorSurfaceView.onClickMapListener() {
    @Override
    public void onClick(PathUnit region) {
    //读取 pathunit
    }
}  
```
该接口是 fps 帧率
```java
view.onFramesListener(new InDoorSurfaceView.FramesListener() {
    @Override
    public void onRefresh(float number) {
    //帧率
    }
}    
```

## 环境:
compileSdkVersion 25     
buildToolsVersion "25.0.2"     
minSdkVersion 16    
gradle plugin:gradle:2.3.1    

## 另外:
如果程序有 bug 和改善方法，感谢提 Issues ，有劳指教!
