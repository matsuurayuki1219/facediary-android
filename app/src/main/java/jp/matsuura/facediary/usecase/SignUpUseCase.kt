package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.repositories.AuthRepository
import jp.matsuura.facediary.enums.CreateUserError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
){

    suspend operator fun invoke(email: String, password: String): Response<Unit, CreateUserError> {
        return authRepository.createUserAccount(email = email, password = password)
    }
}