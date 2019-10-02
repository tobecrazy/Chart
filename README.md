# Chart
Donut chart
### 自己在工作中遇到了自定义chart ，所以写了一个自定义view
aar下载链接： https://github.com/tobecrazy/Chart/blob/master/Chart-release-v1.1.aar 

### 如何使用

layout xml
```
    <cn.dbyl.chart.DonutChart
        android:id="@+id/donut_chart_view"
        android:layout_width="180dp"
        android:layout_height="180dp"
        android:layout_gravity="center"
        android:layout_margin="50dp"
        app:chart_circle_background_color="@color/gray"
        app:chart_circle_progress_color="@color/red"
        app:chart_isAnimation="false"
        app:chart_progress_max_value="100"
        app:chart_progress_value="0"
        app:chart_textColor="@color/black"
        app:chart_textSize="20sp" />
```

### 效果图

<img src="https://raw.githubusercontent.com/tobecrazy/Chart/master/donutchart.gif" alt="loading" width="420">
