package jp.matsuura.facediary.ui.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.repositories.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository,
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
            kotlin.runCatching {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                authRepository.resetPassword(email = email)
                _event.emit(Event.CanReset)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
            }
        }
    }

    data class UiState(
        val isProgressVisible: Boolean,
    )

    sealed class Event {
        object CanReset: Event()
        object NotExistAccount: Event()
        object ValidationError: Event()
        object UnknownError: Event()
        object NetworkError: Event()
    }

}