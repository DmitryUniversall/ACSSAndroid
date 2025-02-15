package com.displaynone.acss.components.auth.models.user.repository

import android.util.Log
import com.displaynone.acss.config.Constants.serverUrl
import com.displaynone.acss.config.Network
import com.displaynone.acss.components.auth.models.user.repository.dto.RegisterUserDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(

) {
    suspend fun isUserExist(login: String): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val encodedLogin = login.encodeURLPath()
            val result = Network.client.get("$serverUrl/api/person/username/$encodedLogin")
            result.status != HttpStatusCode.OK
        }
    }
    suspend fun login(token: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("$serverUrl/api/person/login") {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}: ${result.body<String>()}")
            }
            Unit
        }
    }
    suspend fun register(login: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = Network.client.post("$serverUrl/api/person/register") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        RegisterUserDto(
                            login = login,
                            password = password,
                        )
                    )
                }
                if (result.status != HttpStatusCode.Created) {
                    Log.w("UserRepository", "Status: ${result.status}, Body: ${result.body<String>()}")
                    error("Status ${result.status}: ${result.body<String>()}")
                }
                Unit
            }
        }

}