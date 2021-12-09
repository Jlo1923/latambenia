package com.example.semillaviva.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.semillaviva.util.EventLiveData

class AuthViewModel : ViewModel() {

    private val timerControl = EventLiveData<Unit>()
    val timer: LiveData<Unit> =  timerControl

    init {
        viewModelScope.launch {
            delay(3000)
            timerControl.value = Unit
        }
    }

}
