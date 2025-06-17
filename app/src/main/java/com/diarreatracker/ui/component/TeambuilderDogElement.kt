package com.diarreatracker.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.diarreatracker.R

class TeambuilderDogElement @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    init {
        maxLines = 1
        isSingleLine = true
        ellipsize = null
        textAlignment = TEXT_ALIGNMENT_CENTER
    }
    var dogId: Int = 0
        private set
    var dogName: String = ""
        private set
    var runCount: Int = 0
        private set
    var totalDistance: Float = 0.0F
        private set

    fun bind(summary: DogRunSummary, days: Int){
        dogId = summary.dogID
        dogName = summary.dogName
        runCount = summary.runCount
        totalDistance = summary.totalDistance

        text = dogName

        val runsPerDay = runCount.toFloat() / days
        val bgColorRes = when {
            runsPerDay / days >= 0.9f -> R.color.dog6
            runsPerDay / days >= 0.7f -> R.color.dog5
            runsPerDay / days >= 0.5f -> R.color.dog4
            runsPerDay / days >= 0.3f -> R.color.dog3
            runsPerDay / days >= 0.1f -> R.color.dog2
            else -> R.color.dog1
        }

        setBackgroundColor(ContextCompat.getColor(context, bgColorRes))
    }
}