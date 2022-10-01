package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.data.repositories.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
){

    suspend operator fun invoke() {
        authRepository.logout()
    }

}