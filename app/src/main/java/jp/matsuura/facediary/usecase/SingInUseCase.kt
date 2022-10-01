package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.model.AuthModel
import jp.matsuura.facediary.data.repositories.AuthRepository
import jp.matsuura.facediary.enums.LoginError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SingInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String, password: String): Response<AuthModel, LoginError> {
        return authRepository.login(email = email, password = password)
    }

}