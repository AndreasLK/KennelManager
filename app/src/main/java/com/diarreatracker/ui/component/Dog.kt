package com.diarreatracker.ui.component

data class Dog(
    val dogID: String,
    val dogName: String,
    var runIDs: List<Int>,
    var poopscoreIDs: List<Int>,
    var vaccineIDs: List<Int>,
    var bodyScoreIDs: List<Int>,
    var vetVisitIDs: List<Int>,
    var heatIDs: List<Int>
)

//TODO: MAKE Data class for all of following: DOG(last), Poopscores, Running, Vaccines, Bodyscore, Vet Visists, Heat
//TODO: ALL SHOULD HAVE A DOGID FIELD and a person who pushed the stuff (person who pushed a run of 5km fx)