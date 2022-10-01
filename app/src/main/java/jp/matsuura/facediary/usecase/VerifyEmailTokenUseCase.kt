package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.api.entity.AuthEntity
import jp.matsuura.facediary.data.model.AuthModel
import jp.matsuura.facediary.data.repositories.AuthRepository
import jp.matsuura.facediary.enums.VerifyEmailError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerifyEmailTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(token: String): Response<AuthModel, VerifyEmailError> {
        return authRepository.verifyEmailToken(token = token)
    }

}