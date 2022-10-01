package jp.matsuura.facediary.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.data.repositories.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            val isLogin: Boolean = authRepository.isLogin()
            if (isLogin) {
                _event.emit(Event.IsLogin)
            } else {
                _event.emit(Event.IsNotLogin)
            }
        }
    }

    sealed class Event {
        object IsLogin: Event()
        object IsNotLogin: Event()
    }

}