package com.diarreatracker.ui.component

data class VetVisit(
    val date: String,
    val vetVisitID: Int,
    val dogID: Int,
    val notes: String,
    val author: String
)
