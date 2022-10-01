package jp.matsuura.facediary.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.usecase.CheckLoginStatusUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkLoginStatus: CheckLoginStatusUseCase,
): ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            val isLogin: Boolean = checkLoginStatus()
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