package com.diarreatracker.ui.component

data class ViewState(val x: Float, val y: Float, val width: Int, val height: Int, val id: Int, val rowName: String, val scaleX: Float, val scaleY: Float, val dogs: List<DogItemView>)