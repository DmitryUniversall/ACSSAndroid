package com.displaynone.acss.components.auth.models.user

import com.displaynone.acss.components.auth.models.user.repository.dto.UserDTO

class UserMapper {

    fun fromDto(userDTO: UserDTO): UserEntity {
        val userEntity = UserEntity(
            id = userDTO.id,
            login = userDTO.login,
            position = userDTO.position,
            name = userDTO.name,
            lastVisit = userDTO.lastVisit,
            photo = userDTO.photo
        )
        return userEntity
    }
    fun toDTO(userEntity: UserEntity): UserDTO {
        val userDto = UserDTO(
            id = userEntity.id,
            login = userEntity.login,
            name = userEntity.name,
            photo = userEntity.photo,
            position = userEntity.position,
            lastVisit = userEntity.lastVisit,
        )
        return userDto
    }
}