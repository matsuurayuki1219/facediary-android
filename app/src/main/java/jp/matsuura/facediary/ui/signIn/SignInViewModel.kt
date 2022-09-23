package jp.matsuura.facediary.ui.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.api.entity.ErrorResponse
import jp.matsuura.facediary.extenstion.checkEmailValidation
import jp.matsuura.facediary.extenstion.checkPasswordValidation
import jp.matsuura.facediary.repositories.AuthRepository
import jp.matsuura.facediary.common.Constant
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

    fun onClickSignInButton(email: String, password: String) {
        viewModelScope.launch {

            if (!email.checkEmailValidation()) {
                _event.emit(Event.ValidationMailError)
                return@launch
            }

            if (!password.checkPasswordValidation()) {
                _event.emit(Event.ValidationPasswordError)
                return@launch
            }

            kotlin.runCatching {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                authRepository.login(email = email, password = password)

            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                // save accessToken using Preference
                authRepository.saveAccessToken(it.accessToken)
                _event.emit(Event.CanSignIn)
            }.onFailure {
                Timber.d(it)
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )

                if (it is HttpException) {
                    val errorJsonStr: String? = it.response()?.errorBody()?.string()
                    val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(ErrorResponse::class.java)
                    val errorResponse: ErrorResponse? = adapter.fromJson(errorJsonStr)

                    if (errorResponse == null) {
                        _event.emit(Event.UnknownError)
                        return@onFailure
                    }

                    when (errorResponse.errorCode) {
                        Constant.PASSWORD_ERROR -> {
                            _event.emit(Event.WrongPassword)
                        }
                        Constant.NOT_USER_EXIST -> {
                            _event.emit(Event.NotExistUser)
                        }
                        Constant.MAIN_NOT_VERIFIED -> {
                            _event.emit(Event.MailNotVerified)
                        }
                        else -> {
                            _event.emit(Event.UnknownError)
                        }
                    }

                } else {
                    _event.emit(Event.NetworkError)
                }

            }
        }
    }

    fun onClickForgetButton() {
        viewModelScope.launch {
            _event.emit(Event.ForgetPassword)
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
        object ValidationMailError: Event()
        object ValidationPasswordError: Event()
        object MailNotVerified: Event()
        object WrongPassword: Event()
        object NotExistUser: Event()
        object UnknownError: Event()
        object NetworkError: Event()
        object ForgetPassword: Event()
        object SignUp: Event()
    }
}