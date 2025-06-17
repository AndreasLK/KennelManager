package com.diarreatracker.ui.component

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogRunSummary(
    @SerialName("ID") val dogID: Int,
    @SerialName("DogName") val dogName: String,
    @SerialName("RunCount") val runCount: Int,
    @SerialName("TotalDistance") val totalDistance: Float
)
