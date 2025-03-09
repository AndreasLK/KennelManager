package com.diarreatracker.ui.component

data class Poopscore(
    val date: String,
    val score: Int,
    val poopscoreID: Int,
    val dogID: Int,
    val type: String, //Poopscore added for a group of dogs, or for the individual dog
    val imageRef: String,
    val author: String
)
