package cn.dbyl.chartdemo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.android.synthetic.main.activity_main.*

@AddTrace(name = "onCreateTrace", enabled = true /* optional */)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start.setOnClickListener {
            donut_chart_view.progressValue = 70
            donut_chart_view.isAnimation = true
            donut_chart_view.interval = 3000
        }
        start.contentDescription = "Start"

        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            FirebaseCrashlytics.getInstance().log("Crash log:: click crash button")
            FirebaseCrashlytics.getInstance().checkForUnsentReports()
            FirebaseCrashlytics.getInstance()
                .recordException(ExceptionInInitializerError("Crashed OPS！"))
        }
        crashButton.gravity= Gravity.CENTER
        addContentView(
            crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = true
        val trace = FirebasePerformance.getInstance().newTrace("Debug")
        trace.start()
        trace.putAttribute("experiment", "A")
        trace.incrementMetric("REQUESTS_COUNTER_NAME", 1)
        Handler().postDelayed(Runnable {
            trace.stop()
        }, 3000)
    }
}
