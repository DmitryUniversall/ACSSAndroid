package com.displaynone.acss.ui.auth

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.displaynone.acss.components.auth.models.user.UserServiceST
import com.displaynone.acss.components.auth.models.user.repository.dto.RegisterUserDto
import com.displaynone.acss.ui.fragment.ProfileViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(): ViewModel() {
    private val _action = Channel<Action>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val action = _action.receiveAsFlow()
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    fun login(login: String){
        viewModelScope.launch {
            try {
                UserServiceST.getInstance().login(login).fold(
                    onSuccess = { openProfile() },
                    onFailure = { error ->
                        Log.e("AuthViewModel", "Login failed: ${error.message ?: "Unknown error"}")
                        _errorState.value = error.message ?: "Ошибка входа"
                    }
                )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Connection error: ${e.message}")
                _errorState.value = "Проблемы с подключением. Повторите попытку позже."
            }
        }
    }
    private fun openProfile() {
        viewModelScope.launch {
            _action.send(Action.GotoProfile)
        }
    }
    fun register(registerUserDto: RegisterUserDto){

    }


    sealed interface Action {
        data object GotoProfile : Action
    }

}