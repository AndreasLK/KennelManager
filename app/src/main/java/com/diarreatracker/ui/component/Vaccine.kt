package com.diarreatracker.ui.component

data class Vaccine(
    val dateOfVaccine: String,
    val dateOfEffectiveness: String,
    val dateOfExpiry: String,
    val vaccineID: Int,
    val dogID: Int,
    val type: String,
    val author: String)
