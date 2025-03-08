package com.diarreatracker.ui.component

data class DogRunning(
    val dogID: String,
    val dogName: String,
    val daysRunningWeek: Int,
    val gender: Boolean,
    val heat: Boolean

)

//TODO: MAKE Data class for all of following: DOG(last), Poopscores, Running, Vaccines, Bodyscore, Vet Visists, Heat
//TODO: ALL SHOULD HAVE A DOGID FIELD and a person who pushed the stuff (person who pushed a run of 5km fx)