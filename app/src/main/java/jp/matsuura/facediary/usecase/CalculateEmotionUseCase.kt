package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.model.FaceApiModel
import jp.matsuura.facediary.data.repositories.FaceRepository
import jp.matsuura.facediary.enums.FaceApiError
import java.io.File
import javax.inject.Inject

class CalculateEmotionUseCase @Inject constructor(
    private val faceRepository: FaceRepository,
) {

    suspend operator fun invoke(file: File): Response<FaceApiModel, FaceApiError> {
        return faceRepository.getFaceInfo(file = file)
    }
}