package com.diarreatracker.ui

import ScrollGestureListener
import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.diarreatracker.DogViewDragTouchListener
import com.diarreatracker.DragTouchListener
import com.diarreatracker.MainActivity
import com.diarreatracker.ScaleListener
import com.example.diarreatracker.R

class FreeFlowScreenManager(
    private val context: Context,
    private val zoomableLayout: FrameLayout,
    private val fileHandler: FileHandler, // Add FileHandler dependency
    private val scaleHolder: MainActivity.ScaleHolder,
    private val editPermission: Boolean
) {
    private lateinit var viewManager: ViewManager // Ensure viewManager is included

    @SuppressLint("ClickableViewAccessibility")
    fun initialize() {
        val viewModel = ViewModelProvider(context as MainActivity)[DogSharedViewModel::class.java]

        val dragTouchListener = DragTouchListener(zoomableLayout, 180, scaleHolder, viewModel, editPermission)
        val dogViewDragTouchListener = DogViewDragTouchListener(dragTouchListener,zoomableLayout, 500, scaleHolder, editPermission, context)
        // Initialize ViewManager
        viewManager = ViewManager(zoomableLayout, fileHandler, dragTouchListener, dogViewDragTouchListener,scaleHolder, viewModel, context)

        val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener(zoomableLayout, scaleHolder))
        val gestureDetector = GestureDetector(context, ScrollGestureListener(zoomableLayout))

        zoomableLayout.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            true
        }


        val addButton = zoomableLayout.rootView.findViewById<Button>(R.id.add_element_button)
        val removeButton = zoomableLayout.rootView.findViewById<Button>(R.id.remove_element_button)
        val saveButton = zoomableLayout.rootView.findViewById<Button>(R.id.save_state_button)
        val loadButton = zoomableLayout.rootView.findViewById<Button>(R.id.load_state_button)


        // Button Listeners for dynamic view management
        addButton.setOnClickListener {
            viewManager.addView()
        }

        removeButton.setOnClickListener {
            viewManager.removeLastView()
        }

        saveButton.setOnClickListener {
            viewManager.saveViewStates()
            Toast.makeText(context, "SAVED", Toast.LENGTH_SHORT).show()
        }

        loadButton.setOnClickListener {
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
            addButton.visibility = View.VISIBLE
            removeButton.visibility = View.VISIBLE
            saveButton.visibility = View.VISIBLE
            loadButton.visibility = View.VISIBLE

        }else{
            adminTextTop.visibility = View.INVISIBLE
            adminTextBottom.visibility = View.INVISIBLE
            adminImage.visibility = View.INVISIBLE
            addButton.visibility = View.INVISIBLE
            removeButton.visibility = View.INVISIBLE
            saveButton.visibility = View.INVISIBLE
            loadButton.visibility = View.VISIBLE
        }



    }
}
