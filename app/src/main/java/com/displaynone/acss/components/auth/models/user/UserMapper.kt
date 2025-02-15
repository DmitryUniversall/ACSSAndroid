package com.displaynone.acss.components.auth.models.user

import com.displaynone.acss.components.auth.models.user.repository.dto.UserDTO

class UserMapper {

    fun fromDto(userDTO: UserDTO): UserEntity {
        val userEntity = UserEntity(
            login = userDTO.login,
            password = userDTO.password,
        )
        return userEntity
    }
    fun toDTO(userEntity: UserEntity): UserDTO {
        val userDto = UserDTO(
            login = userEntity.login,
            password = userEntity.password,
        )
        return userDto
    }
}