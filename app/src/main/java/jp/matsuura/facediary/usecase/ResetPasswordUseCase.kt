package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.repositories.AuthRepository
import jp.matsuura.facediary.enums.ResetPasswordError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String): Response<Unit, ResetPasswordError> {
        return authRepository.resetPassword(email = email)
    }
}