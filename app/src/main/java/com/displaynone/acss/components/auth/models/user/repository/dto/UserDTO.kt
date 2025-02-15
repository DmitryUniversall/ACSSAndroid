package com.displaynone.acss.components.auth.models.user.repository.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
)