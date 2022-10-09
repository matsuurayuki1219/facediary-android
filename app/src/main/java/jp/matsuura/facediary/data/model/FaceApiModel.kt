package jp.matsuura.facediary.data.model

import jp.matsuura.facediary.data.api.entity.*

data class FaceApiModel(
    val faceId: String,
    val faceRectangle: FaceRectangleModel,
    val faceAttributes: FaceAttributeModel,
)

data class FaceRectangleModel (
    val top: Int,
    val left: Int,
    val width: Int,
    val height: Int,
)

data class FaceAttributeModel (
    val smile: String,
    val headPose: HeadPoseModel,
    val gender: String,
    val age: Double,
    val facialHair: FacialHairModel,
    val glasses: String,
    val emotion: EmotionModel,
    val blur: BlurModel,
    val exposure: ExposureModel,
    val noise: NoiseModel,
    val makeup: MakeUpModel,
    val accessories: List<String>,
    val occlusion: OcclusionModel,
    val hair: HairModel,
)

data class HeadPoseModel (
    val pitch: Double,
    val roll: Double,
    val yaw: Double,
)

data class BlurModel (
    val blurLevel: String,
    val value: Double,
)

data class EmotionModel (
    val anger: Double,
    val contempt: Double,
    val disgust: Double,
    val fear: Double,
    val happiness: Double,
    val neutral: Double,
    val sadness: Double,
    val surprise: Double,
)

data class ExposureModel (
    val exposureLevel: String,
    val value: Double,
)

data class FacialHairModel (
    val moustache: Double,
    val beard: Double,
    val sideburns: Double,
)

data class HairModel (
    val bald: Double,
    val invisible: Boolean,
    val hairColor: List<HairColorModel>,
)

data class HairColorModel (
    val color: String,
    val confidence: Double,
)

data class MakeUpModel (
    val eyeMakeup: Boolean,
    val lipMakeup: Boolean,
)

data class NoiseModel (
    val noiseLevel: String,
    val value: Double,
)

data class OcclusionModel (
    val foreheadOccluded: Boolean,
    val eyeOccluded: Boolean,
    val mouthOccluded: Boolean,
)

fun FaceApiEntity.toModel() = FaceApiModel(
    faceId = faceId,
    faceRectangle = faceRectangle.toModel(),
    faceAttributes = faceAttributes.toModel(),
)

fun FaceRectangleEntity.toModel() = FaceRectangleModel(
    top = top,
    left = left,
    width = width,
    height = height,
)

fun FaceAttributeEntity.toModel() = FaceAttributeModel(
    smile = smile,
    headPose = headPose.toModel(),
    gender = gender,
    age = age,
    facialHair = facialHair.toModel(),
    glasses = glasses,
    emotion = emotion.toModel(),
    blur = blur.toModel(),
    exposure = exposure.toModel(),
    noise = noise.toModel(),
    makeup = makeup.toModel(),
    accessories = accessories,
    occlusion = occlusion.toModel(),
    hair = hair.toModel(),
)

fun HeadPoseEntity.toModel() = HeadPoseModel(
    pitch = pitch,
    roll = roll,
    yaw = yaw,
)

fun BlurEntity.toModel() = BlurModel(
    blurLevel = blurLevel,
    value = value,
)

fun EmotionEntity.toModel() = EmotionModel(
    anger = anger,
    contempt = contempt,
    disgust = disgust,
    fear = fear,
    happiness = happiness,
    neutral = neutral,
    sadness = sadness,
    surprise = surprise,
)

fun ExposureEntity.toModel() = ExposureModel(
    exposureLevel = exposureLevel,
    value = value,
)

fun FacialHairEntity.toModel() = FacialHairModel(
    moustache = moustache,
    beard = beard,
    sideburns = sideburns,
)

fun HairEntity.toModel() = HairModel(
    bald = bald,
    invisible = invisible,
    hairColor = hairColor.map { it.toModel() },
)

fun HairColorEntity.toModel() = HairColorModel(
    color = color,
    confidence = confidence,
)

fun MakeUpEntity.toModel() = MakeUpModel(
    eyeMakeup = eyeMakeup,
    lipMakeup = lipMakeup,
)

fun NoiseEntity.toModel() = NoiseModel(
    noiseLevel = noiseLevel,
    value = value,
)

fun OcclusionEntity.toModel() = OcclusionModel(
    foreheadOccluded = foreheadOccluded,
    eyeOccluded = eyeOccluded,
    mouthOccluded = mouthOccluded,
)