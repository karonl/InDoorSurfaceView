# InDoorView [![Build Status](https://travis-ci.org/karonl/InDoorSurfaceView.svg?branch=master)](https://travis-ci.org/karonl/InDoorSurfaceView)  [![Jitpack](https://jitpack.io/v/karonl/InDoorSurfaceView.svg)](https://jitpack.io/#karonl/InDoorSurfaceView)
无依赖第三方库的室内户型图交互组件库     
no-dependency Indoor map view library.     

## 目录结构
该 master branch 为完整的演示项目，其中 Sample 为应用模块，InDoorView 为库模块，LICENSE 为版权说明。

## 快速体验
1. 下载apk： https://leanclub.cn/InDoorViewSample.apk    
2. 查看mp4： https://leanclub.cn/Screenrecord20170504.mp4      
3. 示例图片：     
<img width="218" height="389" alt="demo pic" src="https://leanclub.cn/indoorviewdemopic.png" /> 

## 应用场景
该控件可让室内图片上的区域拥有点击事件，可用于**开发电影院选座、商场购物地图、展位摊位在线预定、办公场地租赁工位**等需要操作不规则区域功能。
如果这正是你所需要的，可以点击该库的 Star _(thanks for your star✨)_，便于收藏学习和关注最新动态。

## 快速开始
1. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
  repositories {
    jcenter()
    maven { url 'https://jitpack.io' }
  }
}
```
2. 在 app 应用模块的 build.gradle 引入
```groovy
dependencies {
  compile 'com.github.karonl:InDoorSurfaceView:1.0'
}
```
3. 在 xml 文件中进行组件声明
```xml
<com.karonl.instance.InDoorView
    android:id="@+id/surface"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
4. 在对应的 activity 中进行引用，并通过设置适配器，把底图和区域 list 填入，最后 refreshData();
```java
InDoorView view = (InDoorView)findViewById(R.id.surface);
  ....
adapter.refreshData();
```
## 为何开发 InDoorView？
开发InDoorView的主要目标是解决人与图片中特殊图案的交互需求。

缘起之前我负责的一个app（类似共享短期工位），主要功能是允许用户在室内户型图上选择工位，并且点击某个工位后付费即可把工位点亮。这和我们常见的电影院选座有点不一样，因为大部分区域是非规则图形，里面摆放的也是不同大小、位置不一的多边形，不同房间也无法复用，这自然无法通过循环绘制固定图形来实现交互。最后，我们决定采用最常见的标识有区域的 jpg 图片来做底图以节约制图成本，这意味着我们需要和这些标记区域进行交互。

一开始我使用游戏引擎来做，该需求并不复杂很快得到满足，但为了个简单功能引入整个库非常不明智，并且体积也大了不少，加载速度也受到影响。所以我决定着手开发一个可以精巧的操作图里不规则区域的第三方库。

## InDoorView 的原理？
把读取地图底图 bitmap 和使用 Paint 的钢笔路径集合一同绘制到一个 canvas 上保存，并通过继承 SurfaceView 把 canvas 绘制到双缓画布中，通过 canvas.drawBitmap 实现缩放和移动，重写 view 点击事件结合 Region 判断点击坐标位于哪个区域内，再通过接口反馈事件。

采用把所有图案内容事先缓存到 canvas 的方法，使用非 UI 线程进行绘制，可实现每秒 60 次左右的界面绘制，实现流畅的移动和缩放操作；在没交互情况下，暂停绘制及刷新以节约计算资源。

## 特性
1. 性能较强，绘制达到 60 帧上下
2. 支持缩放以及拖动
3. 直接继承自原生 SurfaceView ，无需导入庞大的引擎库

## 接口说明
Activity 中对 view 的控制代码如下：
```java
InDoorView view = (InDoorView)findViewById(R.id.surface);
DataAdapter adapter =  new DataAdapter();
view.setAdapter(adapter);//初始化
adapter.setBmp(bmp);//设置图片(底图)
adapter.setList(list);//设置数组(图上的可点区域)
adapter.refreshData();
```
    
代码中的 list 的具体设置看这里:(输入钢笔路径需要是图片左上角的相对坐标)
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
     
通过该接口可以返回点击到的区域的 PathUnit 元素，可通过此来获取区域名字等信息
```java
view.setOnClickMapListener(new InDoorView.onClickMapListener() {
    @Override
    public void onClick(PathUnit region) {
    //读取 pathunit
    }
}  
```
     
该接口是 fps 帧率
```java
view.onFramesListener(new InDoorView.FramesListener() {
    @Override
    public void onRefresh(float number) {
    //帧率
    }
}    
```
## 库生产环境:
compileSdkVersion 24     
minSdkVersion 16     

# License
   
Apache License 2.0

