package jp.matsuura.facediary.ui.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.common.extenstion.checkEmailValidation
import jp.matsuura.facediary.common.extenstion.checkPasswordValidation
import jp.matsuura.facediary.enums.CreateUserError
import jp.matsuura.facediary.usecase.SignUpUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUp: SignUpUseCase,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    fun onClickSignUpButton(email: String, password: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                if (!email.checkEmailValidation()) {
                    _event.emit(Event.Failure(error = CreateUserError.EMAIL_FORMAT_ERROR))
                    return@launch
                }
                if (!password.checkPasswordValidation()) {
                    _event.emit(Event.Failure(error = CreateUserError.PASSWORD_FORMAT_ERROR))
                    return@launch
                }
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                signUp(email = email, password = password)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                handleResponse(response = it)
            }.onFailure {
                Timber.d(it)
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

    private suspend fun handleResponse(response: Response<Unit, CreateUserError>) {
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
        data class Failure(val error: CreateUserError): Event()
        object UnknownError: Event()
        object NetworkError: Event()
    }
}