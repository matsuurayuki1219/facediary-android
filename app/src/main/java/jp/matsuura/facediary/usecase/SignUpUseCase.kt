package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.data.api.entity.ApiEntity
import jp.matsuura.facediary.data.repositories.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
){

    suspend operator fun invoke(email: String, password: String): ApiEntity {
        return authRepository.createUserAccount(email = email, password = password)
    }
}