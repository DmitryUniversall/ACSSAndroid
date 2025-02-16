package com.displaynone.acss.ui.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.displaynone.acss.components.auth.models.user.UserServiceST
import com.displaynone.acss.components.auth.models.user.repository.dto.UserDTO
import com.displaynone.acss.databinding.FragmentProfileBinding
import com.displaynone.acss.util.navigateTo
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(): ViewModel() {
    private val _action = Channel<Action>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val action = _action.receiveAsFlow()
    val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()


    fun logout(){
        UserServiceST.getInstance().logout()
    }
    fun getInfo(){
        viewModelScope.launch {
            UserServiceST.getInstance().getInfo().fold(
                onSuccess = { data ->
                    _state.emit(State.Show(data))
                },
                onFailure = { error ->
                    error.message?.let { error(it) }
                    Log.e("ProfileViewModel", error.message.toString())
                }
            )
        }
    }
    fun openAuth(){
        viewModelScope.launch {
            _action.send(Action.GoToAuth)
        }
    }
    sealed interface State {
        data object Loading : State
        data class Show(
            val item: UserDTO
        ) : State

        data object Changed : State
        data class Error(
            val text: String
        ) : State
    }
    sealed interface Action {
        data object GoToAuth: Action
    }
}