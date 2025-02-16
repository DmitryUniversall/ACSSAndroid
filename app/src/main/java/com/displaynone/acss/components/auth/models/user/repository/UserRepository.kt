package com.displaynone.acss.components.auth.models.user.repository

import android.util.Log
import com.displaynone.acss.components.auth.models.user.AuthTokenManager
import com.displaynone.acss.components.auth.models.user.AuthTokenPair
import com.displaynone.acss.config.Constants.serverUrl
import com.displaynone.acss.config.Network
import com.displaynone.acss.components.auth.models.user.repository.dto.RegisterUserDto
import com.displaynone.acss.components.auth.models.user.repository.dto.UserDTO
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlin.math.log

class UserRepository(

) {
    suspend fun isUserExist(login: String): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val encodedLogin = login.encodeURLPath()
            val result = Network.client.get("$serverUrl/api/$encodedLogin/auth/")
            result.status != HttpStatusCode.OK
        }
    }
    suspend fun login(login: String): Result<AuthTokenPair> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("$serverUrl/api/$login") {
                setBody(login)
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}: ${result.body<String>()}")
            }
            Log.d("UserRepository", result.bodyAsText())
            result.body()
        }
    }
    suspend fun getInfo(token: String): Result<UserDTO> = withContext(Dispatchers.IO){
        runCatching {
            val result = Network.client.get("$serverUrl/api/users/me") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}: ${result.body<String>()}")
            }
            Log.d("UserRepository", result.bodyAsText())
            result.body()
        }
    }
    suspend fun openDoor(token: String, code: Long): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.patch("$serverUrl/api/open") {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                setBody("""{"value":$code}""")
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