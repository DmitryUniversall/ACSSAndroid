package com.displaynone.acss.ui.result
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.app.Application
import android.net.http.HttpException
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.displaynone.acss.components.auth.models.user.UserServiceST
import kotlinx.coroutines.launch
import kotlinx.io.IOException

class QrResultViewModel(application: Application) : AndroidViewModel(application) {
    val _state = MutableStateFlow<State>(State.Result(-1))
    val state = _state.asStateFlow()

    fun openDoor(code: Long) {
        viewModelScope.launch {
            try {
                val stringCode = code.toString()
                UserServiceST.getInstance().openDoor(stringCode).fold(
                    onSuccess = { response ->
                        Log.d("QrResultViewModel", "Door opened")
                        _state.emit(State.Result(
                            response
                        ))
                                },
                    onFailure = { error ->
                        Log.e("QrResultViewModel", "Door open failed: ${error.message ?: "Unknown error"}")
                        _state.emit(State.Error(
                            error.message.toString()
                        ))
                    }
                )
            } catch (e: Exception) {
                Log.e("QrResultViewModel", "Unexpected error: ${e.message}")
            }
        }
    }

    sealed interface State {
        data class Result(
            val resultCode: Int
        ): State
        data class Error(
            val errorMessage: String
        ): State
    }
}