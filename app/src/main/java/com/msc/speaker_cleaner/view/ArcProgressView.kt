package com.msc.speaker_cleaner.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.msc.speaker_cleaner.R

class ArcProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var diameter: Float = 0f
    private var height: Float = 0f
    private var radius: Float = 0f
    private var strokeWidth: Float = 0f
    private lateinit var paint: Paint
    private var progress: Float = 0f

    init {
        initializePaint()
    }

    private fun initializePaint() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = ContextCompat.getColor(context, R.color.color_progress)

        val left = (diameter / 2) - radius
        val top = (height / 2) - radius
        val right = (diameter / 2) + radius
        val bottom = (height / 2) + radius

        canvas.drawArc(left, top, right, bottom, 170f, 200f, false, paint)

        paint.color = ContextCompat.getColor(context, R.color.app_main)
        canvas.drawArc(left, top, right, bottom, 170f, maxOf(0.3f, progress * 2.0f), false, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        diameter = w.toFloat()
        height = h.toFloat()
        val min = minOf(diameter, height)
        strokeWidth = min / 16f
        radius = (min / 2) - (strokeWidth / 2)
        paint.strokeWidth = strokeWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = (radius * 2 + strokeWidth).toInt()
        val desiredHeight = (radius * 2 + strokeWidth).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    fun getProgress(): Float {
        return progress
    }

    fun setProgress(value: Float) {
        progress = when {
            value < 0f -> 0f
            value > 100f -> 100f
            else -> value
        }
        invalidate()
    }
}
