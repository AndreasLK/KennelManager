package com.diarreatracker// com.example.diarreatracker.ScaleListener.kt
import android.view.ScaleGestureDetector
import androidx.constraintlayout.widget.ConstraintLayout

class ScaleListener(
    private val zoomableLayout: ConstraintLayout,
    private val scaleHolder: MainActivity.ScaleHolder
) : ScaleGestureDetector.SimpleOnScaleGestureListener() {


    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val dampingFactor = 0.5f
        val adjustedScaleFactor = 1+(detector.scaleFactor -1) * dampingFactor
        val newScaleFactor = (scaleHolder.scaleFactor * adjustedScaleFactor).coerceIn(0.3f, 1f)
        val scaleChange = newScaleFactor / scaleHolder.scaleFactor
        scaleHolder.scaleFactor = newScaleFactor

        val imaginaryBox = floatArrayOf(zoomableLayout.x, zoomableLayout.y, zoomableLayout.width.toFloat(), zoomableLayout.height.toFloat())

        imaginaryBox[2] *= scaleChange
        imaginaryBox[3] *= scaleChange
        imaginaryBox[0] = (zoomableLayout.x + zoomableLayout.width / 2) - imaginaryBox[2] / 2
        imaginaryBox[1] = (zoomableLayout.y + zoomableLayout.height / 2) - imaginaryBox[3] / 2

        for (i in 0 until zoomableLayout.childCount) {
            val child = zoomableLayout.getChildAt(i)
            val relativeX = (child.x) / zoomableLayout.width
            val relativeY = (child.y) / zoomableLayout.height

            child.pivotX = detector.focusX
            child.pivotY = detector.focusY
            child.scaleX *= scaleChange
            child.scaleY *= scaleChange


            val newX = imaginaryBox[0] + (relativeX * imaginaryBox[2])
            val newY = imaginaryBox[1] + (relativeY * imaginaryBox[3])
            child.x = newX
            child.y = newY
        }
        return true
    }

}
