package jp.matsuura.facediary.ui.verify

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.data.api.entity.ErrorEntity
import jp.matsuura.facediary.usecase.SaveAccessTokenUseCase
import jp.matsuura.facediary.usecase.VerifyEmailTokenUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
                // save accessToken using Preference
                saveAccessToken(accessToken = it.accessToken)
                _event.emit(Event.Success)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                handleErrorResponse(throwable = it)
            }
        }
    }

    private suspend fun handleErrorResponse(throwable: Throwable) {
        if (throwable is HttpException) {
            val errorJsonStr: String? = throwable.response()?.errorBody()?.string()
            val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(
                ErrorEntity::class.java)
            val errorResponse: ErrorEntity? = adapter.fromJson(errorJsonStr)

            if (errorResponse == null) {
                _event.emit(Event.UnknownError)
                return
            }

            when (errorResponse.errorCode) {
                "ES03_001" -> {
                    _event.emit(Event.NotVerifyToken)
                }
                else -> {
                    _event.emit(Event.UnknownError)
                }
            }

        } else {
            _event.emit(Event.NetworkError)
        }
    }

    data class UiState(
        val isProgressVisible: Boolean,
    )

    sealed class Event {
        object Success: Event()
        object NotVerifyToken: Event()
        object NetworkError: Event()
        object UnknownError: Event()
    }

}