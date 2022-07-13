package jp.matsuura.facediary.repositories

import jp.matsuura.facediary.api.AuthApi
import jp.matsuura.facediary.datasource.FaceDiaryPreference
import jp.matsuura.facediary.model.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val preference: FaceDiaryPreference,
) {

    suspend fun login(userId: String, password: String): AuthResponse {
        return withContext(Dispatchers.IO) {
            api.login(userId = userId, password = password)
        }
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

    suspend fun createUserAccount(userId: String, password: String): AuthResponse {
        return withContext(Dispatchers.IO) {
            api.createUser(userId = userId, password = password)
        }
    }

}