package jp.matsuura.facediary.ui.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.repositories.AuthRepository
import jp.matsuura.facediary.utils.Constant
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    fun onClickSignInButton(userId: String, password: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                authRepository.login(userId = userId, password = password)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                if (it.status == Constant.SERVER_STATUS_OK) {
                    // save accessToken using Preference
                    authRepository.saveAccessToken(it.accessToken)
                    _event.emit(Event.CanSignIn)
                } else {
                    when (it.errorCode) {
                        Constant.WRONG_PASSWORD -> {
                            _event.emit(Event.WrongPassword)
                        }
                        Constant.NOT_EXIST_USER -> {
                            _event.emit(Event.NotExistUser)
                        }
                        else -> {
                            _event.emit(Event.UnknownError)
                        }
                    }
                }
            }.onFailure {
                Timber.d(it)
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                _event.emit(Event.NetworkError)
            }
        }
    }

    fun onClickSignUpButton() {
        viewModelScope.launch {
            _event.emit(Event.SignUp)
        }
    }

    data class UiState(
        val isProgressVisible: Boolean,
    )

    sealed class Event {
        object CanSignIn: Event()
        object WrongPassword: Event()
        object NotExistUser: Event()
        object UnknownError: Event()
        object NetworkError: Event()
        object SignUp: Event()
    }
}