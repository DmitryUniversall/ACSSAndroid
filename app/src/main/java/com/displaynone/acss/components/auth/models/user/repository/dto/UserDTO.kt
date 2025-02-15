package com.displaynone.acss.components.auth.models.user.repository.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class UserDTO (
    @SerialName("id")
    val id: Long,

    @SerialName("login")
    val login: String,

    @SerialName("name")
    val name: String,

    @SerialName("photo")
    val photo: String,

    @SerialName("position")
    val position: String,

    @SerialName("lastVisit")
    val lastVisit: String,
)