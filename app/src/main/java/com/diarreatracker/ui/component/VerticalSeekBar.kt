package com.diarreatracker.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent

class VerticalSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.seekBarStyle
) : androidx.appcompat.widget.AppCompatSeekBar(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        // Rotate canvas 90 degrees clockwise
        canvas.rotate(90f)
        // Translate canvas so it starts from the correct position
        canvas.translate(-height.toFloat(), 0f)
        super.onDraw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                // Calculate progress based on the vertical position
                val progress = max - (max * event.y / height).toInt()
                this.progress = progress.coerceIn(0, max)
                true
            }
            MotionEvent.ACTION_CANCEL -> return false
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Swap width and height for proper vertical orientation
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }
}
