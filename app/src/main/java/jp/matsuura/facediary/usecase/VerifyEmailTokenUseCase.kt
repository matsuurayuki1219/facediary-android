package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.data.api.entity.AuthEntity
import jp.matsuura.facediary.data.repositories.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerifyEmailTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(token: String): AuthEntity {
        return authRepository.verifyEmailToken(token = token)
    }

}