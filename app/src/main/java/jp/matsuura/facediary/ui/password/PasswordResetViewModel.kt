package jp.matsuura.facediary.ui.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.data.api.entity.ErrorEntity
import jp.matsuura.facediary.common.extenstion.checkEmailValidation
import jp.matsuura.facediary.usecase.ResetPasswordUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val resetPassword: ResetPasswordUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    fun onClickResetButton(email: String) {
        viewModelScope.launch {
            if (!email.checkEmailValidation()) {
                _event.emit(Event.ValidationMailError)
                return@launch
            }
            kotlin.runCatching {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                resetPassword(email = email)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                _event.emit(Event.CanReset)
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
                "ES04_001" -> {
                    _event.emit(Event.ValidationMailError)
                }
                "ES04_002" -> {
                    _event.emit(Event.NotExistAccount)
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
        object CanReset: Event()
        object NotExistAccount: Event()
        object ValidationMailError: Event()
        object UnknownError: Event()
        object NetworkError: Event()
    }

}