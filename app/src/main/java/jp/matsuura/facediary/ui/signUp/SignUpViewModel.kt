package jp.matsuura.facediary.ui.signUp

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
class SignUpViewModel @Inject constructor(
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


    fun onClickSignUpButton(email: String, password: String) {
        viewModelScope.launch {
            kotlin.runCatching {

                if (!email.checkEmailValidation()) {
                    _event.emit(Event.ValidationMailError)
                    return@launch
                }

                if (!password.checkPasswordValidation()) {
                    _event.emit(Event.ValidationPasswordError)
                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                authRepository.createUserAccount(email, password)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                _event.emit(Event.SignUp)
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
                        Constant.USER_ALREADY_EXISTED -> {
                            _event.emit(Event.UserAlreadyExisted)
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

    data class UiState(
        val isProgressVisible: Boolean,
    )

    sealed class Event {
        object SignUp: Event()
        object UserAlreadyExisted: Event()
        object ValidationMailError: Event()
        object ValidationPasswordError: Event()
        object UnknownError: Event()
        object NetworkError: Event()
    }
}