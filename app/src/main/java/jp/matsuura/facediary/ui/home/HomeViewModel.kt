package jp.matsuura.facediary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.common.extenstion.checkEmailValidation
import jp.matsuura.facediary.common.extenstion.checkPasswordValidation
import jp.matsuura.facediary.enums.CreateUserError
import jp.matsuura.facediary.ui.verify.VerifyEmailViewModel
import jp.matsuura.facediary.usecase.LogoutUseCase
import jp.matsuura.facediary.usecase.SignUpUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logout: LogoutUseCase,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    fun onLogoutButtonClicked() {
        viewModelScope.launch {
            kotlin.runCatching {
                logout()
            }.onSuccess {
                _event.emit(Event.Logout)
            }.onFailure {
                Timber.d(it)
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
        object Logout: Event()
    }
}