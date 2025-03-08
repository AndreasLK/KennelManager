package com.diarreatracker// com.example.diarreatracker.DragTouchListener.kt
import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.diarreatracker.ui.BlockDialog
import com.diarreatracker.ui.DogSharedViewModel
import com.diarreatracker.ui.component.CustomTextView
import kotlin.math.absoluteValue
import kotlin.math.sqrt

class DragTouchListener(
    private val zoomableLayout: ConstraintLayout,
    private val snapThreshold: Int = 50,
    private val scaleHolder: MainActivity.ScaleHolder,
    private val viewModel: DogSharedViewModel,
    private val editPermission: Boolean
    ) : View.OnTouchListener {

    private var dX = 0f
    private var dY = 0f
    private var startX = 0f
    private var startY = 0f
    private val snappingDistance = snapThreshold*scaleHolder.scaleFactor
    private val clickThreshold = 7
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
                startX = event.rawX
                startY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = (event.rawX) + dX
                val newY = (event.rawY) + dY

                view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start()
            }
            MotionEvent.ACTION_UP -> {
                val distanceX = (event.rawX - startX).toInt()
                val distanceY = (event.rawY - startY).toInt()

                if (distanceX.absoluteValue < clickThreshold && distanceY.absoluteValue < clickThreshold) {
                    val dialog = BlockDialog(view.context as MainActivity, view.id, view as CustomTextView, viewModel)
                    dialog.showDialog()
                } else {
                    snapToClosestView(view)
                }
            }
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun snapToClosestView(view: View) {
        var closestView: View? = null
        var minDistance = Float.MAX_VALUE

        // Find the closest view within the snapping threshold
        for (i in 0 until zoomableLayout.childCount) {
            val otherView = zoomableLayout.getChildAt(i)
            if (otherView != view) {
                val distance = calculateDistance(view, otherView)
                if (distance < snapThreshold * scaleHolder.scaleFactor && distance < minDistance) {
                    minDistance = distance
                    closestView = otherView
                }
            }
        }

        // Perform snapping if a close enough view is found
        closestView?.let { target ->
            var snapX = view.x
            var snapY = view.y

            //Check if Horizontal or vertical snapping is intended:
            //VERTICES = 0 Top, 1 Left, 2, Bottom, 3 Right
            val targetEdges = getSidesOfView(target)
            val viewEdges = getSidesOfView(view)

            var shortestDistance = Float.MAX_VALUE
            var shortestDistanceEdge = 0
            for (i in viewEdges.indices){
                val distance = calculateDistanceCoordinates(viewEdges[i], targetEdges[(i + 2) % 4])

                if (distance < shortestDistance){
                    shortestDistanceEdge = i
                    shortestDistance = distance
                }
            }


            when (shortestDistanceEdge) {
                0 -> {

                    snapX = target.x
                    snapY = target.y + target.height * target.scaleY
                    Log.d("SNAPPING", "TOP TO BOTTOM")
                }
                1 -> {
                    snapX = target.x + target.width * target.scaleX
                    snapY = target.y
                    Log.d("SNAPPING", "LEFT TO RIGHT")
                }
                2 ->{
                    snapX = target.x
                    snapY = target.y - view.height * view.scaleY
                    Log.d("SNAPPING", "BOTTOM TO TOP")
                }
                3 ->{

                    snapX = target.x - view.width * view.scaleX
                    snapY = target.y
                    Log.d("SNAPPING", "RIGHT TO LEFT")
                }
            }
            // Once snapping is done, update rowName
            if (view is CustomTextView && target is CustomTextView) {
                // Set the rowName of the dragged view to match the target view's rowName
                view.rowName = target.rowName
                // Update the text to reflect the new rowName
                view.reload()
            }



            // Apply the new snap position with animation
            view.animate()
                .x(snapX)
                .y(snapY)
                .setDuration(100) // animate snapping for smoothness
                .start()
        }
    }
    private fun getSidesOfView(view:View): Array<FloatArray>{
        val top = floatArrayOf(view.x + (view.width / 2) * view.scaleX, view.y)
        val left = floatArrayOf(view.x, view.y + (view.height / 2) * view.scaleY)
        val bottom = floatArrayOf(view.x + (view.width / 2) * view.scaleX, view.y + view.height * view.scaleY)
        val right = floatArrayOf(view.x + view.width * view.scaleX, view.y + (view.height / 2) * view.scaleY)

        return arrayOf(top, left, bottom, right)
    }

    private fun calculateDistance(view1: View, view2: View): Float {
        val dx = (view1.x - view2.x).toDouble()
        val dy = (view1.y - view2.y).toDouble()
        return sqrt(dx * dx + dy * dy).toFloat()
    }

    private fun calculateDistanceCoordinates(coordinate1: FloatArray, coordinate2: FloatArray): Float {
        val dx = coordinate1[0] - coordinate2[0]
        val dy = coordinate1[1] - coordinate2[1]
        return sqrt(dx * dx + dy * dy)
    }

}
