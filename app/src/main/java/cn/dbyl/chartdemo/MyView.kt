package cn.dbyl.chartdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Create by i321533 (young.liu@sap.com) on 10/03/2019
 **/
class MyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var paint: Paint = Paint()
    companion object {
        const val TAG: String = "MyView"
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.v(TAG, "onFinishInflate")
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.v(TAG, "onLayout")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.v(TAG, "onMeasure")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.v(TAG, "onDraw")
        paint.style=Paint.Style.FILL
        paint.strokeWidth=20f
        paint.color = context.getColor(R.color.blue)
        canvas?.translate(400f, 50f)
        canvas?.drawCircle(200f, 200f, 200f, paint)
    }

    @SuppressLint("GetContentDescriptionOverride")
    override fun getContentDescription(): CharSequence {
        if (paint.textSize>16)
        {
            return " "
        }
        return super.getContentDescription()
    }
}