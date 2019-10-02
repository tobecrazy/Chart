package cn.dbyl.chart

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.min

/**
 * Create by Young on 09/29/2019
 **/
class DonutChart @JvmOverloads constructor(
    context: Context,
    var mAttributeSet: AttributeSet? = null
) : View(context, mAttributeSet) {
    companion object {
        const val TAG: String = "DonutChart"
    }

    private lateinit var firstCirclePaint: Paint
    private lateinit var secondCirclePaint: Paint
    private lateinit var textPaint: Paint
    //default height & width is 100dp
    private val defaultValue: Float = 100f
    private var isHasDivider: Boolean = false
    private var rectF = RectF()
    private var circleWidth: Float = 0f
    private var strokeWidth: Float = 0f

    private var animatedVal: Float = 0f
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    var progressMaxValue: Int = 0
        set(value) {
            field = if (value in 1..100) {
                value
            } else {
                100
            }
            postInit()
        }
    var progressValue: Int = 0
        set(value) {
            field = if (value in 0..progressMaxValue) {
                value
            } else {
                0
            }
            postInit()
        }

    var circleBackgroundColor: Int? = null
        set(value) {
            field = value
            postInit()
        }

    var circleProgressColor: Int? = null
        set(value) {
            field = value
            postInit()
        }
    var textColor: Int? = null
        set(value) {
            field = value
            postInit()
        }
    var textSize: Float? = null
        set(value) {
            field = value
            postInit()
        }
    var dividerText: String? = null
        set(value) {
            if (null == value) {
                isHasDivider = false
            } else {
                field = value
                isHasDivider = true
            }
            postInit()
        }

    var isAnimation: Boolean = false

    var interval: Int = 3000
        set(value) {
            if (isAnimation) {
                if (value in 1000..10000) {
                    field = value
                    postInit()
                    setCurrentAnimation(isAnimation, interval.toLong())
                }

            }
        }

    init {
        initAttribute()
    }

    @SuppressLint("Recycle", "CustomViewStyleable")
    private fun initAttribute() {
        val typedArray = context.obtainStyledAttributes(
            mAttributeSet,
            R.styleable.ChartView, 0, 0
        )
        try {
            progressMaxValue = typedArray.getInt(
                R.styleable.ChartView_chart_progress_max_value,
                100
            )
            progressValue = typedArray.getInt(
                R.styleable.ChartView_chart_progress_value,
                0
            )
            circleBackgroundColor = typedArray.getColor(
                R.styleable.ChartView_chart_circle_background_color,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
            circleProgressColor = typedArray.getColor(
                R.styleable.ChartView_chart_circle_progress_color,
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            textColor = typedArray.getColor(
                R.styleable.ChartView_chart_textColor,
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            textSize = typedArray.getDimension(
                R.styleable.ChartView_chart_textSize,
                resources.getDimension(R.dimen.text_micro_size)
            )
            dividerText = typedArray.getString(R.styleable.ChartView_chart_divider_text)
            isAnimation = typedArray.getBoolean(R.styleable.ChartView_chart_isAnimation, false)
            interval = typedArray.getInt(R.styleable.ChartView_chart_interval, 3000)
        } catch (e: Exception) {
            Log.e(TAG, "avoid initial failed")
        } finally {
            typedArray.recycle()
        }
    }


    @JvmOverloads
    fun setCurrentAnimation(isAnimation: Boolean, during: Long = 3000) {
        Log.v(TAG, "setCurrentAnimation $animatedVal")
        if (isAnimation) {
            val animator = ValueAnimator.ofFloat(0f, animatedVal)
            if (during in 100..10000) {
                animator.duration = during
            } else {
                //default is 3000
                animator.duration = 3000
            }
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.addUpdateListener { animation ->
                animatedVal = animation.animatedValue as Float
                postInvalidate()
            }
            animator.start()
        } else {
            Log.v(TAG, "setCurrentAnimation $animatedVal")
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        Log.v(TAG, "onDraw $mWidth, $mHeight")
        rectF.set(
            getSide(mWidth, mHeight) + circleWidth / 2,
            getSide(mWidth, mHeight) + circleWidth / 2,
            mWidth.toFloat() - getSide(mWidth, mHeight).toFloat() - circleWidth / 2,
            mWidth.toFloat() - getSide(mWidth, mHeight).toFloat() - circleWidth / 2
        )
        //draw first circle
        canvas?.drawArc(rectF, 0f, 360f, false, firstCirclePaint)

        //draw second circle
        val degree = 360f * 0.01f * animatedVal
        //rotate screen -90
        canvas?.rotate(-90f, (width / 2).toFloat(), (height / 2).toFloat())
        canvas?.drawArc(rectF, 0f, degree, false, secondCirclePaint)
        //rotate back
        canvas?.rotate(90f, (width / 2).toFloat(), (height / 2).toFloat())


        val baseline =
            mHeight / 2 - (textPaint.fontMetrics.descent + textPaint.fontMetrics.ascent) / 2
        textPaint.textSize = 1.3f * (textSize ?: 0f)
        textPaint.alpha = 255
        if (isHasDivider) {
            canvas?.drawText(
                formatProgressValue(animatedVal),
                (mWidth / 2 - (progressValue.toString().length * (textSize ?: 0f)) * 0.65f),
                baseline,
                textPaint
            )
            textPaint.textSize = 0.9f * (textSize ?: 0f)
            textPaint.alpha = 100
            canvas?.drawText(dividerText ?: "", (mWidth / 2).toFloat(), baseline, textPaint)
            canvas?.drawText(
                progressMaxValue.toString(),
                (mWidth / 2 + (progressValue.toString().length * (textSize ?: 0f)) * 0.6f),
                baseline,
                textPaint
            )
        } else {
            canvas?.drawText(
                formatProgressValue(animatedVal),
                (mWidth / 2).toFloat(),
                baseline,
                textPaint
            )
        }

    }

    private fun getSide(a: Int, b: Int): Int {
        return if (a > b) abs(a - b) / 2 else 0
    }


    private fun postInit() {
        //in case progress value is large than max value
        if (progressValue > progressMaxValue) {
            progressMaxValue = progressValue
        }
        animatedVal = 100.0f * (progressValue.toFloat() / progressMaxValue.toFloat())
        initPains()
        invalidate()
    }

    private fun initPains() {
        initFirstCirclePaint()
        initSecondCirclePaint()
        initTextPaint()
    }

    private fun initFirstCirclePaint() {
        Log.v(TAG, "First circleWidth $circleWidth")
        firstCirclePaint = Paint()
        firstCirclePaint.isAntiAlias = true
        firstCirclePaint.isDither = true
        firstCirclePaint.color = circleBackgroundColor ?: 0
        firstCirclePaint.strokeWidth = strokeWidth
        firstCirclePaint.style = Paint.Style.STROKE
        firstCirclePaint.strokeWidth = circleWidth
    }

    private fun initSecondCirclePaint() {
        Log.v(TAG, "Second circleWidth $circleWidth")
        secondCirclePaint = Paint()
        secondCirclePaint.isAntiAlias = true
        secondCirclePaint.isDither = true
        secondCirclePaint.color = circleProgressColor ?: 0
        secondCirclePaint.strokeWidth = strokeWidth
        secondCirclePaint.style = Paint.Style.STROKE
        secondCirclePaint.strokeWidth = circleWidth

    }

    private fun initTextPaint() {
        Log.v(TAG, "init Text Paint")
        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.isDither = true
        textPaint.color = textColor ?: 0
        textPaint.style = Paint.Style.STROKE
        textPaint.strokeCap = Paint.Cap.ROUND
        textPaint.textAlign = Paint.Align.CENTER
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        var result = dpToPx(defaultValue)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        when (heightMode) {
            MeasureSpec.AT_MOST
            -> result = min(result, heightSize)
            MeasureSpec.EXACTLY -> result = heightSize
            MeasureSpec.UNSPECIFIED -> {
                Log.v(TAG, "return default $result")
            }
        }
        return result
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        var result = dpToPx(defaultValue)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        when (widthMode) {
            MeasureSpec.AT_MOST -> result = min(result, widthSize)
            MeasureSpec.EXACTLY -> result = widthSize
            MeasureSpec.UNSPECIFIED -> {
                Log.v(TAG, "return default $result")
            }
        }
        return result
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measureWidth(widthMeasureSpec)
        mHeight = measureHeight(heightMeasureSpec)
        circleWidth = min(mWidth, mHeight) / 2 * 0.15f
        Log.v(TAG, "onMeasure ==== mWidth ===> $mWidth  mHeight====> $mHeight ===> circleWidth ===>$circleWidth")
        initPains()
    }

    private fun formatProgressValue(progress: Float): String {
        return progress.toInt().toString()
    }

    fun dpToPx(dp: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}