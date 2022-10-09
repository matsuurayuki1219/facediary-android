package jp.matsuura.facediary.ui.auth.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.common.extenstion.checkEmailValidation
import jp.matsuura.facediary.common.extenstion.checkPasswordValidation
import jp.matsuura.facediary.data.model.AuthModel
import jp.matsuura.facediary.enums.LoginError
import jp.matsuura.facediary.usecase.SaveAccessTokenUseCase
import jp.matsuura.facediary.usecase.SingInUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signIn: SingInUseCase,
    private val saveAccessToken: SaveAccessTokenUseCase,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    fun onClickSignInButton(email: String, password: String) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isProgressVisible = true,
            )

            if (!email.checkEmailValidation()) {
                _event.emit(Event.Failure(error = LoginError.EMAIL_FORMAT_ERROR))
                return@launch
            }
            if (!password.checkPasswordValidation()) {
                _event.emit(Event.Failure(error = LoginError.PASSWORD_FORMAT_ERROR))
                return@launch
            }
            kotlin.runCatching {
                signIn(email = email, password = password)
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

    private suspend fun handleResponse(response: Response<AuthModel, LoginError>) {
        when (response) {
            is Response.Success -> {
                saveAccessToken(accessToken = response.value.accessToken)
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
        data class Failure(val error: LoginError): Event()
        object UnknownError: Event()
        object NetworkError: Event()
    }
}