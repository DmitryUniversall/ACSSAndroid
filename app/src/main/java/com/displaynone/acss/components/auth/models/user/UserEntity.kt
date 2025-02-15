package com.displaynone.acss.components.auth.models.user

data class UserEntity(
    val id: Long,
    val login: String,
    val name: String,
    val photo: String,
    val position: String,
    val lastVisit: String,
)