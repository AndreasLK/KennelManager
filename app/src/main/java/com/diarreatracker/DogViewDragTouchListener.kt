package com.diarreatracker

import android.content.Context
import android.content.res.ColorStateList
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.diarreatracker.ui.component.CustomTextView
import com.diarreatracker.ui.component.DogItemView
import com.example.diarreatracker.R
import kotlin.math.sqrt

class DogViewDragTouchListener(
    private val dragTouchListener: DragTouchListener,
    private val zoomableLayout: FrameLayout,
    private val snapThreshold: Int = 500,
    private val scaleHolder: MainActivity.ScaleHolder,
    private val editPermission: Boolean,
    private val context: Context

    ) : View.OnTouchListener {

        private var dX = 0f
        private var dY = 0f

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action){
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
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
                val closestView = getClosestView(view)
                if (closestView is CustomTextView) {
                    if (calculateDistance(
                            view,
                            closestView
                        ) < snapThreshold * scaleHolder.scaleFactor
                    ) {
                        val gender = view.findViewById<ImageView>(R.id.genderSymbolMale).isVisible

                        val dogItemView = DogItemView(
                            dogname = view.findViewById<EditText>(R.id.dogNameDisplay).text.toString(),
                            gender = gender,
                            dateOfBirth = view.findViewById<EditText>(R.id.ageDisplay).text.toString(),
                            castrationText = view.findViewById<EditText>(R.id.sterilizationTextDisplay).text.toString(),
                            bodyScore = view.findViewById<EditText>(R.id.bodyScoreDisplay).text.toString(),
                            heat = view.findViewById<ImageView>(R.id.dogUnitBackground).imageTintList != ColorStateList.valueOf(
                                ContextCompat.getColor(context, R.color.dog_view_standard))
                        )

                        val current: MutableList<DogItemView> = closestView.dogs.toMutableList()
                        current.add(dogItemView)

                        closestView.dogs = current.toList()
                        closestView.reload()

                        zoomableLayout.removeView(view)

                        //
                    }
                }
            }
        }
        return true
    }


    private fun getClosestView(view: View): View? {
        var closestView: View? = null
        var minDistance = Float.MAX_VALUE

        for (i in 0 until zoomableLayout.childCount){
            val otherView = zoomableLayout.getChildAt(i)
            if (otherView != view){
                val distance = calculateDistance(view, otherView)
                if (distance < snapThreshold * scaleHolder.scaleFactor && distance < minDistance && otherView is CustomTextView){
                    minDistance = distance
                    if (distance < 150) {closestView = otherView}
                }
            }
        }

        return closestView
    }

    private fun calculateDistance(view1: View, view2: View): Float {
        val x1 = view1.x + view1.width / 2
        val y1 = view1.y + view1.height / 2
        val x2 = view2.x + view2.width / 2
        val y2 = view2.y + view2.height / 2

        val dx = (x1 - x2).toDouble()
        val dy = (y1 - y2).toDouble()
        return sqrt(dx * dx + dy * dy).toFloat()
    }





}