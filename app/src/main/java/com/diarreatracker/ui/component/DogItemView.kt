package com.diarreatracker.ui.component

data class DogItemView(
    var dogname: String,
    var gender: Boolean,
    var dateOfBirth: String,
    var castrationText: String,
    var bodyScore: String,
    var heat: Boolean,
    var runCount: Int = 2
)