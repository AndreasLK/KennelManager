package com.diarreatracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diarreatracker.ui.component.DogItemView

class DogSharedViewModel : ViewModel() {
    private val _addDogEvent = MutableLiveData<DogItemView>()
    val addDogEvent: LiveData<DogItemView> get() = _addDogEvent

    fun addDog(dog: DogItemView) {
        _addDogEvent.value = dog
    }
}
