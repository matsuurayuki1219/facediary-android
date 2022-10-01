package jp.matsuura.facediary.data.repositories

import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.api.AuthApi
import jp.matsuura.facediary.data.api.entity.ApiEntity
import jp.matsuura.facediary.data.api.entity.AuthEntity
import jp.matsuura.facediary.data.api.entity.ErrorEntity
import jp.matsuura.facediary.data.api.getErrorResponse
import jp.matsuura.facediary.data.api.request.ChangePasswordRequest
import jp.matsuura.facediary.data.api.request.SignUpRequest
import jp.matsuura.facediary.data.api.toModel
import jp.matsuura.facediary.data.datasource.FaceDiaryPreference
import jp.matsuura.facediary.data.model.AuthModel
import jp.matsuura.facediary.data.model.toModel
import jp.matsuura.facediary.enums.CreateUserError
import jp.matsuura.facediary.enums.LoginError
import jp.matsuura.facediary.enums.ResetPasswordError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val preference: FaceDiaryPreference,
) {

    suspend fun login(email: String, password: String): Response<AuthModel, LoginError> {
        return withContext(Dispatchers.IO) {
            val response = try {
                api.login(email = email, password = password)
            } catch (e: IOException) {
                return@withContext Response.Error(LoginError.NETWORK_ERROR)
            }
            if (response.isSuccessful) {
                Response.Success(response.toModel { it.toModel() })
            } else {
                val errorBody = response.getErrorResponse<ErrorEntity>()
                if (errorBody == null) {
                    throw HttpException(response)
                } else {
                    val errorType = when (errorBody.errorCode) {
                        "ES01_001" -> LoginError.EMAIL_FORMAT_ERROR
                        "ES01_002" -> LoginError.PASSWORD_FORMAT_ERROR
                        "ES01_003" -> LoginError.USER_NOT_EXIST
                        "ES01_004" -> LoginError.MAIL_NOT_VERIFIED
                        "ES01_005" -> LoginError.PASSWORD_WRONG
                        else -> throw HttpException(response)
                    }
                    Response.Error(errorType)
                }
            }
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

    suspend fun createUserAccount(email: String, password: String): Response<Unit, CreateUserError> {
        return withContext(Dispatchers.IO) {
            val body= SignUpRequest(
                email = email,
                password = password,
            )
            val response = try {
                api.createUser(body = body)
            } catch (e: IOException) {
                return@withContext Response.Error(CreateUserError.NETWORK_ERROR)
            }
            if (response.isSuccessful) {
                Response.Success(Unit)
            } else {
                val errorBody = response.getErrorResponse<ErrorEntity>()
                if (errorBody == null) {
                    throw HttpException(response)
                } else {
                    val errorType = when (errorBody.errorCode) {
                        "ES02_001" -> CreateUserError.EMAIL_FORMAT_ERROR
                        "ES02_002" -> CreateUserError.PASSWORD_FORMAT_ERROR
                        "ES02_003" -> CreateUserError.USER_ALREADY_EXIST
                        else -> throw HttpException(response)
                    }
                    Response.Error(errorType)
                }
            }
        }
    }

    suspend fun verifyEmailToken(token: String): AuthEntity {
        return withContext(Dispatchers.IO) {
            api.verifyToken(token = token)
        }
    }

    suspend fun resetPassword(email: String): Response<Unit, ResetPasswordError> {
        return withContext(Dispatchers.IO) {
            val response = try {
                api.resetPassword(email = email)
            } catch (e: IOException) {
                return@withContext Response.Error(ResetPasswordError.NETWORK_ERROR)
            }
            if (response.isSuccessful) {
                Response.Success(Unit)
            } else {
                val errorBody = response.getErrorResponse<ErrorEntity>()
                if (errorBody == null) {
                    throw HttpException(response)
                } else {
                    val errorType = when (errorBody.errorCode) {
                        "ES04_001" -> ResetPasswordError.EMAIL_FORMAT_ERROR
                        "ES04_002" -> ResetPasswordError.USER_NOT_EXIST
                        else -> throw HttpException(response)
                    }
                    Response.Error(errorType)
                }
            }
        }
    }

    suspend fun changePassword(email: String, password: String, token: String): ApiEntity {
        return withContext(Dispatchers.IO) {
            val request = ChangePasswordRequest(
                email = email,
                password = password,
                token = token,
            )
            api.changePassword(body = request)
        }
    }

}