package com.diarreatracker

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.diarreatracker.ui.BlockDialog
import com.diarreatracker.ui.DogSharedViewModel
import com.diarreatracker.ui.component.CustomTextView
import kotlin.math.absoluteValue
import kotlin.math.sqrt

class DragTouchListener(
    private val zoomableContent: FrameLayout, // Only the inner zoomable FrameLayout
    private val snapThreshold: Int = 50,
    private val scaleHolder: MainActivity.ScaleHolder,
    private val viewModel: DogSharedViewModel,
    private val editPermission: Boolean
) : View.OnTouchListener {

    private var downRawX = 0f
    private var downRawY = 0f
    private var viewDownX = 0f
    private var viewDownY = 0f
    private val clickThreshold = 7

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downRawX = event.rawX
                downRawY = event.rawY
                viewDownX = view.x
                viewDownY = view.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (editPermission) {
                    val deltaX = event.rawX - downRawX
                    val deltaY = event.rawY - downRawY

                    view.x = viewDownX + deltaX / scaleHolder.scaleFactor
                    view.y = viewDownY + deltaY / scaleHolder.scaleFactor
                }
            }
            MotionEvent.ACTION_UP -> {
                val distanceX = (event.rawX - downRawX).toInt()
                val distanceY = (event.rawY - downRawY).toInt()

                if (distanceX.absoluteValue < clickThreshold && distanceY.absoluteValue < clickThreshold) {
                    val dialog = BlockDialog(view.context as MainActivity, view.id, view as CustomTextView, viewModel)
                    dialog.showDialog()
                } else if(editPermission) {
                    snapToClosestView(view)
                }
            }
        }
        return true
    }

    private fun snapToClosestView(view: View) {
        var closestView: View? = null
        var minDistance = Float.MAX_VALUE

        for (i in 0 until zoomableContent.childCount) {
            val otherView = zoomableContent.getChildAt(i)
            if (otherView != view) {
                val distance = calculateDistance(view, otherView)
                if (distance < snapThreshold * scaleHolder.scaleFactor && distance < minDistance) {
                    minDistance = distance
                    closestView = otherView
                }
            }
        }

        closestView?.let { target ->
            var snapX = view.x
            var snapY = view.y

            val targetEdges = getSidesOfView(target)
            val viewEdges = getSidesOfView(view)

            var shortestDistance = Float.MAX_VALUE
            var shortestDistanceEdge = 0
            for (i in viewEdges.indices) {
                val distance = calculateDistanceCoordinates(viewEdges[i], targetEdges[(i + 2) % 4])
                if (distance < shortestDistance) {
                    shortestDistance = distance
                    shortestDistanceEdge = i
                }
            }

            // Snap based on edge
            when (shortestDistanceEdge) {
                0 -> {
                    snapX = target.x
                    snapY = target.y + target.height
                }
                1 -> {
                    snapX = target.x + target.width
                    snapY = target.y
                }
                2 -> {
                    snapX = target.x
                    snapY = target.y - view.height
                }
                3 -> {
                    snapX = target.x - view.width
                    snapY = target.y
                }
            }

            if (view is CustomTextView && target is CustomTextView) {
                view.rowName = target.rowName
                view.reload()
            }

            view.animate()
                .x(snapX)
                .y(snapY)
                .setDuration(100)
                .start()
        }
    }

    private fun getSidesOfView(view: View): Array<FloatArray> {
        val halfWidth = (view.width) / 2f
        val halfHeight = (view.height) / 2f

        val top = floatArrayOf(view.x + halfWidth, view.y)
        val left = floatArrayOf(view.x, view.y + halfHeight)
        val bottom = floatArrayOf(view.x + halfWidth, view.y + view.height)
        val right = floatArrayOf(view.x + view.width, view.y + halfHeight)

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
