package cn.dbyl

import android.app.Application
import com.didichuxing.doraemonkit.DoraemonKit

/**
 * Create by i321533 (young.liu@sap.com) on 08/21/2020
 **/
class ChartApp:Application() {

    override fun onCreate() {
        super.onCreate()
        DoraemonKit.install(this)
    }
}