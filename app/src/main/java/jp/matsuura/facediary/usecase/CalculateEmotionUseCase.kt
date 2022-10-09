package jp.matsuura.facediary.usecase

import jp.matsuura.facediary.data.model.FaceApiModel
import jp.matsuura.facediary.data.repositories.FaceRepository
import java.io.File
import javax.inject.Inject

class CalculateEmotionUseCase @Inject constructor(
    private val faceRepository: FaceRepository,
) {

    suspend operator fun invoke(file: File): List<FaceApiModel> {
        return faceRepository.getFaceInfo(file = file)
    }
}