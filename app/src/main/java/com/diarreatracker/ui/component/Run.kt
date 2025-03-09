package com.diarreatracker.ui.component

data class Run(
    val date: String,
    val runID: Int,
    val distance: Int,
    val dogID: Int,
    val type: String, //Sled // Cart // Freerun etc.
    val GPXRef: String,
    val author: String
)
