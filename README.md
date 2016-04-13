# InDoorSurfaceView Android 室内地图
InDoorSurfaceView是使用Android Surface开发的室内地图模块，正好最近做室内这方面的内容，所以把它整理出来。性能较强，绘制达到60帧上下，操作流畅。
直接使用原生surface，无需导入庞大的引擎。

使用说明
```java
InDoorSurfaceView view = (InDoorSurfaceView)findViewById(R.id.surface);
DataAdapter adapter =  new DataAdapter();
view.setAdapter(adapter);//初始化
adapter.setBmp(bmp);//设置图片(底图)
adapter.setList(list);//设置数组(图上的可点区域)
adapter.refreshData();
```
接口说明
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
绘制到图上的不规则可点图形例子
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

如何使用该库
1.通过设置导入
2.去除启动器intent fliter
