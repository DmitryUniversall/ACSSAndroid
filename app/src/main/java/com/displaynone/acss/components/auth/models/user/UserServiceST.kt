package com.displaynone.acss.components.auth.models.user

import com.displaynone.acss.components.auth.models.user.repository.UserRepository

class UserServiceST(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) {
    private var instance: UserServiceST? = null

    fun createInstance() {

    }
    fun getInstance(): UserServiceST {
        TODO()
    }
}