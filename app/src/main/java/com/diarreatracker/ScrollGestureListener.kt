import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout

class ScrollGestureListener(private val zoomableLayout: FrameLayout) : GestureDetector.SimpleOnGestureListener() {
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        // Apply translation to each child view based on the accumulated scroll distance
        for (i in 0 until zoomableLayout.childCount) {
            val child = zoomableLayout.getChildAt(i)
            child.x -= distanceX
            child.y -= distanceY
        }

        return true
    }
}