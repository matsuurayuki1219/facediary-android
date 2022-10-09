package jp.matsuura.facediary.ui.auth.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.common.extenstion.checkEmailValidation
import jp.matsuura.facediary.enums.ResetPasswordError
import jp.matsuura.facediary.usecase.ResetPasswordUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
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
                _event.emit(Event.Failure(error = ResetPasswordError.EMAIL_FORMAT_ERROR))
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

    private suspend fun handleResponse(response: Response<Unit, ResetPasswordError>) {
        when (response) {
            is Response.Success -> {
                _event.emit(Event.Success)
            }
            is Response.Error -> {
                _event.emit(Event.Failure(error = response.error))
            }
        }
    }

    data class UiState(
        val isProgressVisible: Boolean,
    )

    sealed class Event {
        object Success: Event()
        data class Failure(val error: ResetPasswordError): Event()
        object UnknownError: Event()
        object NetworkError: Event()
    }

}