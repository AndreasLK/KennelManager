package com.diarreatracker

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diarreatracker.ui.GanglineAdapter
import com.example.diarreatracker.R

class TeamBuilderScreenManager(
    private val context: Context,
    private val layout: ConstraintLayout
) {

    private val gangline = layout.rootView.findViewById<RecyclerView>(R.id.teamBuilderSledRecycler)
    private val ganglineManager = LinearLayoutManager(context).apply {
        reverseLayout = true
        stackFromEnd = true

    }

    fun initialize(){
        gangline.layoutManager = ganglineManager
        gangline.adapter = GanglineAdapter(5)


    }
}