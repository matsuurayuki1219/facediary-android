package jp.matsuura.facediary.ui.auth.change_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.enums.ChangePasswordError
import jp.matsuura.facediary.usecase.ChangePasswordUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePassword: ChangePasswordUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    private val args = ChangePasswordFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val token: String = args.token
    private val email: String = args.email

    fun onClickChangeButton(password: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                changePassword(email = email, password = password, token = token)
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

    private suspend fun handleResponse(response: Response<Unit, ChangePasswordError>) {
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
        data class Failure(val error: ChangePasswordError): Event()
        object UnknownError: Event()
        object NetworkError: Event()
    }

}