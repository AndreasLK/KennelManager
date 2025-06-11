package com.diarreatracker.ui.component

data class Dog(
    val dogID: String,
    val dogName: String,
    val gender: Boolean,
    val sterilized: Boolean,
    val cageID: Int,

    val parents: List<Int>,
    val children: List<Int>,

    var runIDs: List<Int>,
    var poopscoreIDs: List<Int>,
    var vaccineIDs: List<Int>,
    var bodyScoreIDs: List<Int>,
    var vetVisitIDs: List<Int>,
    var heatIDs: List<Int>
)