package cn.dbyl.chartdemo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start.setOnClickListener{
            donut_chart_view.progressValue=70
            donut_chart_view.isAnimation=true
            donut_chart_view.interval=3000
        }
        start.contentDescription="Start"
    }
}
