package jp.matsuura.facediary.repositories

import jp.matsuura.facediary.api.AuthApi
import jp.matsuura.facediary.api.entity.ApiResponse
import jp.matsuura.facediary.api.entity.AuthResponse
import jp.matsuura.facediary.api.request.LoginRequest
import jp.matsuura.facediary.datasource.FaceDiaryPreference

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val preference: FaceDiaryPreference,
) {

    suspend fun login(email: String, password: String): AuthResponse {
        return withContext(Dispatchers.IO) {
            api.login(email = email, password = password)
        }
    }

    suspend fun logout() {
        preference.clearAll()
    }

    suspend fun isLogin(): Boolean {
        return withContext(Dispatchers.IO) {
            preference.accessToken.isNotEmpty()
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        return withContext(Dispatchers.IO) {
            preference.accessToken = accessToken
        }
    }

    suspend fun createUserAccount(email: String, password: String): ApiResponse {
        return withContext(Dispatchers.IO) {
            val body= LoginRequest(
                email = email,
                password = password,
            )
            api.createUser(body = body)
        }
    }

    suspend fun verifyToken(verifyToken: String): AuthResponse {
        return withContext(Dispatchers.IO) {
            api.verifyToken(verifyToken = verifyToken)
        }
    }

    suspend fun resetPassword(email: String): ApiResponse {
        return withContext(Dispatchers.IO) {
            api.resetPassword(email = email)
        }
    }

    suspend fun changePassword(email: String, oldPassword: String, newPassword: String, passwordToken: String): ApiResponse {
        return withContext(Dispatchers.IO) {
            api.changePassword(
                email = email,
                oldPassword = oldPassword,
                newPassword = newPassword,
                passwordToken = passwordToken
            )
        }
    }

}