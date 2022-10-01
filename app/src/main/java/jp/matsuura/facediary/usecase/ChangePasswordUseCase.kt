package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.data.repositories.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String, token: String, password: String) {
        authRepository.changePassword(email = email, password = password, token = token)
    }
}