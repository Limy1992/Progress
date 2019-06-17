package com.lmy.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.NonNull

/**
 * CreateDate:2019/6/17
 * Author:lmy
 */
class SeekBarMoreProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mOnSeekBarMoreProgressListener: OnSeekBarMoreProgressListener? = null
    private val mPaint = Paint()
    //测量的宽度
    private var measureViewWidth = 0
    //自定义thumb
    private lateinit var thumb: Drawable
    private var mThumbWidth = 0
    private var mThumbHeight = 0
    //存储进度参数数据
    private lateinit var progressList: MutableList<MergeAppendProgress>
    //存储数量
    private var progressListSize = 1
    //进度条最大值
    private var progressMaxValue = 100L
    //当前进度
    private var progress = 0L
    //屏幕内滑动的X坐标值
    private var screenMoveValue = 0
    //进度条的宽度
    private var recodeProgressWidth: Int = 0

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.Progress)
        thumb = resources.getDrawable(attributes.getInt(R.styleable.Progress_thumb, R.mipmap.ic_p_t))
        attributes.recycle()
        mPaint.color = Color.RED
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = dip2px(6f).toFloat()
        mPaint.strokeCap = Paint.Cap.ROUND
        mThumbWidth = thumb.intrinsicWidth
        mThumbHeight = thumb.intrinsicHeight
        initProgressValue()


        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("SeekBarMoreProgress","ACTION_DOWN")
                    mOnSeekBarMoreProgressListener?.onStartTrackingTouch(this)
                }
                MotionEvent.ACTION_MOVE -> {
                    screenMoveValue = (event.x).toInt()
                    progress = screenMoveValue * progressMaxValue / recodeProgressWidth
                    Log.d("SeekBarMoreProgress", progress.toString())
                    if (screenMoveValue in 1 until recodeProgressWidth) {
                        mOnSeekBarMoreProgressListener?.onProgressChanged(this, progress)
                        invalidate()
                    }
                }

                MotionEvent.ACTION_UP -> {
                    Log.d("SeekBarMoreProgress","ACTION_UP")
                    mOnSeekBarMoreProgressListener?.onStopTrackingTouch(this)
                }
            }
            return@setOnTouchListener true
        }
    }

    /**
     * 初始化默认参数
     */
    private fun initProgressValue() {
        progressList = ArrayList()
        progressList.add(MergeAppendProgress(100, Color.BLUE))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureViewWidth = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(measuredWidth, mThumbHeight)
        recodeProgressWidth = getProgressWidth()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var recodeStartX = 0L
        for (i in 0 until progressListSize) {
            val appendProgress = progressList[i]
            //每个进度的值
            val progressValue = appendProgress.progressValue
            //进度在当前View的值
            val screenProgressValue = (progressValue * measureViewWidth / progressMaxValue) + recodeStartX
            mPaint.color = appendProgress.progressColor
            if (i == 0) {
                if (progressListSize == 1) {
                    canvas.drawLine(
                        (mThumbWidth shr 1).toFloat(),
                        (mThumbHeight shr 1).toFloat(),
                        (progressValue - (mThumbWidth shr 1)).toFloat(),
                        (mThumbHeight shr 1).toFloat(),
                        mPaint
                    )
                } else {
                    canvas.drawLine(
                        (mThumbWidth shr 1).toFloat(),
                        (mThumbHeight shr 1).toFloat(),
                        progressValue.toFloat(),
                        (mThumbHeight shr 1).toFloat(),
                        mPaint
                    )
                }

            } else {
                if (i == progressListSize - 1) {
                    mPaint.strokeCap = Paint.Cap.ROUND
                    canvas.drawLine(
                        recodeStartX.toFloat(),
                        (mThumbHeight shr 1).toFloat(),
                        (progressValue - (mThumbWidth shr 1)).toFloat(),
                        (mThumbHeight shr 1).toFloat(),
                        mPaint
                    )
                } else {
                    mPaint.strokeCap = Paint.Cap.SQUARE
                    canvas.drawLine(
                        recodeStartX.toFloat(),
                        (mThumbHeight shr 1).toFloat(),
                        progressValue.toFloat(),
                        (mThumbHeight shr 1).toFloat(),
                        mPaint
                    )
                }
            }
            recodeStartX = screenProgressValue
        }
        thumb.setBounds(screenMoveValue, 0, mThumbWidth + screenMoveValue, mThumbHeight)
        thumb.draw(canvas)
    }


    /**
     * 设置进度条参数
     * @param progressList 进度条参存储
     */
    fun setProgressModel(@NonNull progressList: MutableList<MergeAppendProgress>) {
        this.progressList = progressList
        this.progressListSize = progressList.size
        recodeProgressWidth = getProgressWidth()
        screenMoveValue = 0
        invalidate()
    }

    fun setProgressMaxValue(progressMaxValue: Long) {
        this.progressMaxValue = progressMaxValue
    }

    fun getProgressMaxValue(): Long {
        return progressMaxValue
    }

    fun getProgress(): Long {
        return progress
    }

    private fun getProgressWidth(): Int {
        return measureViewWidth - mThumbWidth
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun setOnSeekBarMoreProgressListener(onSeekBarMoreProgressListener: OnSeekBarMoreProgressListener) {
        this.mOnSeekBarMoreProgressListener = onSeekBarMoreProgressListener
    }

    interface OnSeekBarMoreProgressListener {
        fun onProgressChanged(seekBar: SeekBarMoreProgress, progress: Long) {}
        fun onStartTrackingTouch(seekBar: SeekBarMoreProgress) {}
        fun onStopTrackingTouch(seekBar: SeekBarMoreProgress) {}
    }
}