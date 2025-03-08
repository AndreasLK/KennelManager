package com.diarreatracker.ui

import ScrollGestureListener
import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.diarreatracker.DogViewDragTouchListener
import com.diarreatracker.DragTouchListener
import com.diarreatracker.MainActivity
import com.diarreatracker.ScaleListener
import com.example.diarreatracker.R

class FreeFlowScreenManager(
    private val context: Context,
    private val zoomableLayout: ConstraintLayout,
    private val fileHandler: FileHandler, // Add FileHandler dependency
    private val scaleHolder: MainActivity.ScaleHolder,
    private val editPermission: Boolean
) {
    private lateinit var viewManager: ViewManager // Ensure viewManager is included

    @SuppressLint("ClickableViewAccessibility")
    fun initialize() {
        val viewModel = ViewModelProvider(context as MainActivity)[DogSharedViewModel::class.java]

        val dragTouchListener = DragTouchListener(zoomableLayout, 180, scaleHolder, viewModel, editPermission)
        val dogViewDragTouchListener = DogViewDragTouchListener(zoomableLayout, 500, scaleHolder, editPermission)
        // Initialize ViewManager
        viewManager = ViewManager(zoomableLayout, fileHandler, dragTouchListener, dogViewDragTouchListener,scaleHolder, viewModel)

        val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener(zoomableLayout, scaleHolder))
        val gestureDetector = GestureDetector(context, ScrollGestureListener(zoomableLayout))

        zoomableLayout.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            true
        }

        // Button Listeners for dynamic view management
        zoomableLayout.rootView.findViewById<Button>(R.id.add_element_button).setOnClickListener {
            viewManager.addView()
        }

        zoomableLayout.rootView.findViewById<Button>(R.id.remove_element_button).setOnClickListener {
            viewManager.removeLastView()
        }

        zoomableLayout.rootView.findViewById<Button>(R.id.save_state_button).setOnClickListener {
            viewManager.saveViewStates()
            Toast.makeText(context, "SAVED", Toast.LENGTH_SHORT).show()
        }

        zoomableLayout.rootView.findViewById<Button>(R.id.load_state_button).setOnClickListener {
            viewManager.addViewFromStorage()
            Toast.makeText(context, "LOADED", Toast.LENGTH_SHORT).show()
        }

        val adminTextTop = zoomableLayout.rootView.findViewById<TextView>(R.id.adminModeTopText)
        val adminTextBottom = zoomableLayout.rootView.findViewById<TextView>(R.id.adminModeBottomText)
        val adminImage = zoomableLayout.rootView.findViewById<ImageView>(R.id.adminModeBackground)

        if (editPermission){
            adminTextTop.visibility = View.VISIBLE
            adminTextBottom.visibility = View.VISIBLE
            adminImage.visibility = View.VISIBLE
        }else{
            adminTextTop.visibility = View.INVISIBLE
            adminTextBottom.visibility = View.INVISIBLE
            adminImage.visibility = View.INVISIBLE
        }



    }
}
