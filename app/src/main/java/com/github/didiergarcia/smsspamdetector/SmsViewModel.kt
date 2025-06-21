package com.github.didiergarcia.smsspamdetector

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SmsViewModel : ViewModel() {
    private val _sender = MutableStateFlow("Waiting for SMS...")
    private val _message = MutableStateFlow("")
    private val _result = MutableStateFlow<String?>(null)

    val sender: StateFlow<String> = _sender
    val message: StateFlow<String> = _message
    val result: StateFlow<String?> = _result

    fun updateSms(sender: String, message: String, result: String) {
        _sender.value = sender
        _message.value = message
        _result.value = result
    }

    companion object {
        val instance = SmsViewModel()
    }
}
