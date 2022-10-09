package jp.matsuura.facediary.data.api.entity

import com.squareup.moshi.JsonClass
import jp.matsuura.facediary.data.model.BlurModel

@JsonClass(generateAdapter = true)
data class FaceApiEntity(
    val faceId: String,
    val faceRectangle: FaceRectangleEntity,
    val faceAttributes: FaceAttributeEntity,
)

@JsonClass(generateAdapter = true)
data class FaceRectangleEntity (
    val top: Int,
    val left: Int,
    val width: Int,
    val height: Int,
)

@JsonClass(generateAdapter = true)
data class FaceAttributeEntity (
    val smile: String,
    val headPose: HeadPoseEntity,
    val gender: String,
    val age: Double,
    val facialHair: FacialHairEntity,
    val glasses: String,
    val emotion: EmotionEntity,
    val blur: BlurEntity,
    val exposure: ExposureEntity,
    val noise: NoiseEntity,
    val makeup: MakeUpEntity,
    val accessories: List<AccessoriesEntity>,
    val occlusion: OcclusionEntity,
    val hair: HairEntity,
)

@JsonClass(generateAdapter = true)
data class AccessoriesEntity (
    val type: String,
    val confidence: Double,
)

@JsonClass(generateAdapter = true)
data class HeadPoseEntity (
    val pitch: Double,
    val roll: Double,
    val yaw: Double,
)

@JsonClass(generateAdapter = true)
data class BlurEntity (
    val blurLevel: String,
    val value: Double,
)

@JsonClass(generateAdapter = true)
data class EmotionEntity (
    val anger: Double,
    val contempt: Double,
    val disgust: Double,
    val fear: Double,
    val happiness: Double,
    val neutral: Double,
    val sadness: Double,
    val surprise: Double,
)

@JsonClass(generateAdapter = true)
data class ExposureEntity (
    val exposureLevel: String,
    val value: Double,
)

@JsonClass(generateAdapter = true)
data class FacialHairEntity (
    val moustache: Double,
    val beard: Double,
    val sideburns: Double,
)

@JsonClass(generateAdapter = true)
data class HairEntity (
    val bald: Double,
    val invisible: Boolean,
    val hairColor: List<HairColorEntity>,
)

@JsonClass(generateAdapter = true)
data class HairColorEntity (
    val color: String,
    val confidence: Double,
)

@JsonClass(generateAdapter = true)
data class MakeUpEntity (
    val eyeMakeup: Boolean,
    val lipMakeup: Boolean,
)

@JsonClass(generateAdapter = true)
data class NoiseEntity (
    val noiseLevel: String,
    val value: Double,
)

@JsonClass(generateAdapter = true)
data class OcclusionEntity (
    val foreheadOccluded: Boolean,
    val eyeOccluded: Boolean,
    val mouthOccluded: Boolean,
)