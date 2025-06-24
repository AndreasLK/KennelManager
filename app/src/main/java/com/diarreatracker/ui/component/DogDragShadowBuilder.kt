package com.diarreatracker.ui.component

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.view.View
import androidx.core.graphics.drawable.toDrawable

class DogDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
    private val shadow = Color.LTGRAY.toDrawable()

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width = view.width
        val height = view.height
        shadow.setBounds(0, 0, width, height)
        size.set(width, height)
        touch.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas) {
        shadow.draw(canvas)
    }
}