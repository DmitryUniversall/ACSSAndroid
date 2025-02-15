package com.displaynone.acss.components.auth.models.user

import android.content.Context
import com.displaynone.acss.components.auth.models.user.repository.UserRepository


@Suppress("UNREACHABLE_CODE")
class UserServiceST(
    private val tokenManager: AuthTokenManager,
) {
    private val userRepository: UserRepository = UserRepository()
    private var instance: UserServiceST? = null

    companion object {
        private var instance: UserServiceST? = null

        fun createInstance(context: Context) {
            if (instance == null) {
                val tokenManager = AuthTokenManager(context)
                instance = UserServiceST(tokenManager)
            }
        }

        fun getInstance(): UserServiceST? {
            return instance
        }
    }
    suspend fun login(login: String): Result<Unit>{
        userRepository.login(login = login).fold(
            onSuccess = { data ->
                tokenManager.saveTokens(data) },
            onFailure = error(message = "Login error")
        )
    }
    fun logout(){
        tokenManager.clear()
    }
}