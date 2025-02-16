package com.displaynone.acss.components.auth.models.user

import android.content.Context
import android.util.Log
import com.displaynone.acss.components.auth.models.user.repository.UserRepository
import com.displaynone.acss.components.auth.models.user.repository.dto.UserDTO
import io.ktor.client.statement.bodyAsText


class UserServiceST(
    private val tokenManager: AuthTokenManager,
) {
    private val userRepository: UserRepository = UserRepository()

    companion object {
        private var instance: UserServiceST? = null

        fun createInstance(context: Context) {
            if (instance == null) {
                val tokenManager = AuthTokenManager(context)
                instance = UserServiceST(tokenManager)
            }
        }

        fun getInstance(): UserServiceST {
            return instance ?: throw RuntimeException("null instance")
        }
    }
    suspend fun login(login: String): Result<Unit>{
        return runCatching {
            userRepository.login(login = login).getOrThrow().let { data ->
                tokenManager.saveTokens(data)
            }
        }
    }
    fun logout(){
        tokenManager.clear()
    }
    suspend fun getInfo(): Result<UserDTO>{
        if (!tokenManager.hasTokens()) {
            throw RuntimeException("access token is null")
        }
        return userRepository.getInfo(tokenManager.authTokenPair!!.accessToken)
    }
}