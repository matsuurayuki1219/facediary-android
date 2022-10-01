package jp.matsuura.facediary.ui.verify

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.api.entity.ErrorEntity
import jp.matsuura.facediary.data.model.AuthModel
import jp.matsuura.facediary.enums.VerifyEmailError
import jp.matsuura.facediary.usecase.SaveAccessTokenUseCase
import jp.matsuura.facediary.usecase.VerifyEmailTokenUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val verifyEmailToken: VerifyEmailTokenUseCase,
    private val saveAccessToken: SaveAccessTokenUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    private val args = VerifyEmailFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val token = args.token

    init {
        checkVerifyToken()
    }

    fun checkVerifyToken() {
        viewModelScope.launch {
            kotlin.runCatching {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                verifyEmailToken(token = token)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                handleResponse(response = it)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                if (it is IOException) {
                    _event.emit(Event.NetworkError)
                } else {
                    _event.emit(Event.UnknownError)
                }
            }
        }
    }

    private suspend fun handleResponse(response: Response<AuthModel, VerifyEmailError>) {
         when (response) {
             is Response.Success -> {
                 saveAccessToken(accessToken = response.value.accessToken)
                 _event.emit(Event.Success)
             }
             is Response.Error -> {
                 if (response.error == VerifyEmailError.NETWORK_ERROR) {
                     _event.emit(Event.NetworkError)
                 } else {
                     _event.emit(Event.Failure(error = response.error))
                 }
             }
         }
    }

    data class UiState(
        val isProgressVisible: Boolean,
    )

    sealed class Event {
        object Success: Event()
        data class Failure(val error: VerifyEmailError): Event()
        object NetworkError: Event()
        object UnknownError: Event()
    }

}