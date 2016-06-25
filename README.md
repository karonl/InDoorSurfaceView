## 案例
先给个简单的介绍，最近接到个案子，大概就是开发一个可以室内选座的app。这我们很容易想到电影院的选座位，可有点不一样，因为每个区域都是不同大小并且位置不一，而我们手上的材料只有一张标识有区域的jpg地图。一开始我使用引擎来做，结果可想而知，为了个简单功能引入整个库非常不明智。

## 介绍
InDoorSurfaceView 就是为这个而设计，他的原理是通过渲染出图片并通过区域边界计算出我们 touch point 在哪个区域。即，你点击了哪个座位。当然事先需要将区域边界传入。

## 特性
InDoorSurfaceView 是继承 SurfaceView 开发的室内地图模块，正好最近做室内这方面的内容，所以把它整理出来。
1、性能较强，绘制达到60帧上下
2、支持缩放拖动
3、直接使用原生surface，无需导入庞大的引擎

## 使用
### 初始化
```java
InDoorSurfaceView view = (InDoorSurfaceView)findViewById(R.id.surface);
DataAdapter adapter =  new DataAdapter();
view.setAdapter(adapter);//初始化
adapter.setBmp(bmp);//设置图片(底图)
adapter.setList(list);//设置数组(图上的可点区域)
adapter.refreshData();
```
### 接口说明
```java
view.setOnClickMapListener(new InDoorSurfaceView.onClickMapListener() {
    @Override
    public void onClick(PathUnit region) {
        //读取pathunit
    }
}            
```
```java
view.onFramesListener(new InDoorSurfaceView.FramesListener() {
    @Override
    public void onRefresh(float number) {
        //刷新率
    }
}    
```
### 绘制到图上的不规则可点图形例子
(输入图形定点相对于图片的坐标)
```java
//图案列表
private void getUnitList(){
    PathUnit unit = new PathUnit(getList());
    unit.setName("你点击到我了");
    unitList.add(unit);
}
//每个图案的坐标组合
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
```
注：从资源读取的图片对应的坐标要乘上desity，网络加载的图片则不用

