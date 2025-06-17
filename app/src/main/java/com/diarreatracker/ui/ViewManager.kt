package com.diarreatracker.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color.LTGRAY
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.diarreatracker.DogViewDragTouchListener
import com.diarreatracker.DragTouchListener
import com.diarreatracker.MainActivity
import com.diarreatracker.ui.component.CustomTextView
import com.diarreatracker.ui.component.DogItemView
import com.diarreatracker.ui.component.ViewState
import com.example.diarreatracker.R

class ViewManager(
    private val layout: FrameLayout,
    private val fileHandler: FileHandler,
    private val dragTouchListener: DragTouchListener,
    private val dogViewDragTouchListener: DogViewDragTouchListener,
    private val scaleHolder: MainActivity.ScaleHolder,
    viewModel: DogSharedViewModel,
    private val context: Context
) {

    private var viewCount = 0
    private val backgroundColors = listOf(R.color.dog_view_heat, R.color.dog_view_balls, R.color.dog_view_standard)

    init {
        viewModel.addDogEvent.observe(this.layout.context as MainActivity) { dog ->
            addDogView(dog)
        }
    }
    private fun Boolean.toInt() = if (this) 1 else 0

    fun addView() {
        val initialSize = (100 * layout.resources.displayMetrics.density).toInt()
        val newView = CustomTextView(layout.context).apply {
            id = View.generateViewId()
            rowName = "Not Assigned"
            dogs = listOf(DogItemView("Isak", true, "20180423", "Castrated", "6+", false))
            setBackgroundColor(LTGRAY)
            setPadding(20, 20, 20, 20)
            layoutParams = ConstraintLayout.LayoutParams(initialSize, initialSize)
            setOnTouchListener(dragTouchListener)
        }

        layout.addView(newView)
    }

    fun removeLastView() {
        if (layout.childCount > 0) {
            layout.removeViewAt(layout.childCount - 1)
            viewCount--
        }
    }

    private fun addDogView(dogItemView: DogItemView){
        val inflater = LayoutInflater.from(layout.context)
        val dogView = inflater.inflate(R.layout.dog_unit, layout, false)

        dogView.id = View.generateViewId()

        val layoutParams = ConstraintLayout.LayoutParams(
            (400 * layout.resources.displayMetrics.density).toInt(),
            (120 * layout.resources.displayMetrics.density).toInt()
        )

        dogView.layoutParams = layoutParams

        dogView.scaleX = (scaleHolder.scaleFactor - 0.3).toFloat()
        dogView.scaleY = (scaleHolder.scaleFactor - 0.3).toFloat()


        dogView.findViewById<EditText>(R.id.dogNameDisplay).setText(dogItemView.dogname)
        dogView.findViewById<EditText>(R.id.ageDisplay).setText(dogItemView.dateOfBirth)
        dogView.findViewById<EditText>(R.id.sterilizationTextDisplay).setText(dogItemView.castrationText)
        dogView.findViewById<EditText>(R.id.bodyScoreDisplay).setText(dogItemView.bodyScore)
        dogView.findViewById<ImageButton>(R.id.editDogButton).visibility = View.INVISIBLE
        dogView.findViewById<ImageButton>(R.id.moveDogButton).visibility = View.INVISIBLE

        dogView.findViewById<EditText>(R.id.dogNameDisplay).isFocusable = false
        dogView.findViewById<EditText>(R.id.ageDisplay).isFocusable = false
        dogView.findViewById<EditText>(R.id.sterilizationTextDisplay).isFocusable = false
        dogView.findViewById<EditText>(R.id.bodyScoreDisplay).isFocusable = false



        if (dogItemView.gender) {
            dogView.findViewById<ImageView>(R.id.genderSymbolMale).visibility = View.VISIBLE
            dogView.findViewById<ImageView>(R.id.genderSymbolFemale).visibility = View.INVISIBLE
        } else {
            dogView.findViewById<ImageView>(R.id.genderSymbolFemale).visibility = View.VISIBLE
            dogView.findViewById<ImageView>(R.id.genderSymbolMale).visibility = View.INVISIBLE
        }

        val background = dogView.findViewById<ImageView>(R.id.dogUnitBackground)
        if (dogItemView.heat){
            background.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context,backgroundColors[dogItemView.gender.toInt()]))
        } else {
            background.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, backgroundColors[2]))
        }

        dogView.setOnTouchListener(dogViewDragTouchListener) //Should get separate dragTouchListener
        layout.addView(dogView)


    }

    fun saveViewStates() {
        val viewStates = mutableListOf<ViewState>()
        for (childNumber in 0 until layout.childCount) {
            val child = layout.getChildAt(childNumber)
            val viewState = ViewState(
                x = child.x,
                y = child.y,
                width = child.width,
                height = child.height,
                id = child.id,
                rowName = (child as? CustomTextView)?.rowName ?: "Unknown",
                scaleX = child.scaleX,
                scaleY = child.scaleY,
                dogs = (child as? CustomTextView)?.dogs ?: emptyList()
            )
            viewStates.add(viewState)
        }
        fileHandler.saveViewStates(viewStates)
        Toast.makeText(layout.context, "SAVED", Toast.LENGTH_SHORT).show()
    }

    fun addViewFromStorage(days: Int) {
        layout.removeAllViews()
        val viewStates = fileHandler.loadViewStates()
        for (viewState in viewStates) {
            val newView = CustomTextView(layout.context).apply {
                id = viewState.id
                rowName = viewState.rowName
                text = ""
                x = viewState.x
                y = viewState.y
                setBackgroundColor(LTGRAY)
                setPadding(20, 20, 20, 20)
                layoutParams = ConstraintLayout.LayoutParams(viewState.width, viewState.height)
                dogs = viewState.dogs

                val sortedDogs = dogs.sortedBy { it.runCount.toFloat() / days }
                dogColors = sortedDogs.map { dog ->
                    when (dog.runCount.toFloat() / days) {
                        in 0.9f..1f -> ContextCompat.getColor(context, R.color.dog6)
                        in 0.7f..0.9f -> ContextCompat.getColor(context, R.color.dog5)
                        in 0.5f..0.7f -> ContextCompat.getColor(context, R.color.dog4)
                        in 0.3f..0.5f -> ContextCompat.getColor(context, R.color.dog3)
                        in 0.1f..0.3f -> ContextCompat.getColor(context, R.color.dog2)
                        else -> ContextCompat.getColor(context, R.color.dog1)
                    }
                }

                setOnTouchListener(dragTouchListener)
            }
            newView.reload()
            Log.d("VIEWSTATE LOAD", "${newView.width}, ${newView.height} = ${viewState.width}, ${viewState.height}")
            layout.addView(newView)
        }
    }
}
