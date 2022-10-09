package jp.matsuura.facediary.ui.register

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.model.EmotionModel
import jp.matsuura.facediary.data.model.FaceApiModel
import jp.matsuura.facediary.enums.FaceApiError
import jp.matsuura.facediary.ui.auth.change_password.ChangePasswordViewModel
import jp.matsuura.facediary.usecase.CalculateEmotionUseCase
import jp.matsuura.facediary.usecase.StoreCacheFileUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ShootCameraViewModel @Inject constructor(
    private val calculateEmotion: CalculateEmotionUseCase,
    private val storeCacheFile: StoreCacheFileUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    private val _faceApiEvent: MutableSharedFlow<FaceApiEvent> = MutableSharedFlow<FaceApiEvent>()
    val faceApiEvent: SharedFlow<FaceApiEvent> = _faceApiEvent.asSharedFlow()

    private var file: File? = null

    fun onCameraFinish(bitmap: Bitmap) {
        viewModelScope.launch {
            kotlin.runCatching {
                _uiState.value = _uiState.value.copy(isProgressVisible = true)
                file = storeCacheFile(fileName = "test", bitmap = bitmap)
                calculateEmotion(file = file!!)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(isProgressVisible = false)
                handleResponse(response = it)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isProgressVisible = false)
                if (it is IOException) {
                    _faceApiEvent.emit(FaceApiEvent.NetworkError)
                } else {
                    _faceApiEvent.emit(FaceApiEvent.UnknownError)
                }
            }
        }
    }

    private suspend fun handleResponse(response: Response<FaceApiModel, FaceApiError>) {
        when (response) {
            is Response.Success -> {
                _faceApiEvent.emit(FaceApiEvent.Success(emotion = response.value.faceAttributes.emotion))
            }
            is Response.Error -> {
                _faceApiEvent.emit(FaceApiEvent.Failure(error = response.error))
            }
        }
    }

    data class UiState(
        val isProgressVisible: Boolean,
    )

    sealed class FaceApiEvent {
        data class Success(val emotion: EmotionModel): FaceApiEvent()
        data class Failure(val error: FaceApiError): FaceApiEvent()
        object UnknownError: FaceApiEvent()
        object NetworkError: FaceApiEvent()
    }

    sealed class Event {
        object Success: Event()
        object Failure: Event()
        object UnknownError: Event()
        object NetworkError: Event()
    }
}