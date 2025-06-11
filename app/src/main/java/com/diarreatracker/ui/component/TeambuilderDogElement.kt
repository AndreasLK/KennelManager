package com.diarreatracker.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TeambuilderDogElement @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    var dogId: Int = 0
        private set
    var dogName: String = ""
        private set
    var runCount: Int = 0
        private set
    var totalDistance: Int = 0
        private set

    fun bind(summary: DogRunSummary){
        dogId = summary.dogID
        runCount = summary.runCount
        totalDistance = summary.totalDistance

        text = dogName
    }
}