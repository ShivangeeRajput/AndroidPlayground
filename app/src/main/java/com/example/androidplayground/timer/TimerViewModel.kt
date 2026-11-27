package com.example.androidplayground.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// BINA service: rotation pe bachta hai, app band to timer band
class TimerViewModel : ViewModel() {
    val timer = Timer(viewModelScope)
}
