package com.displaynone.acss.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.displaynone.acss.components.auth.models.user.UserServiceST
import com.displaynone.acss.components.auth.models.user.repository.dto.RegisterUserDto
import com.displaynone.acss.ui.fragment.ProfileViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(): ViewModel() {
    private val _action = Channel<Action>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val action = _action.receiveAsFlow()

    fun login(login: String){
        viewModelScope.launch {
            UserServiceST.getInstance()?.login(login)
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