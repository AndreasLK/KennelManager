package com.diarreatracker.ui.component

data class Cage(
    val cageID: Int,
    var cageRow: String,
    var height: Int,
    var width: Int,
    var scaleX: Float,
    var scaleY: Float,
    var longtitude: Float,
    var latitude: Float,
    var dogs: List<Dog>
)
