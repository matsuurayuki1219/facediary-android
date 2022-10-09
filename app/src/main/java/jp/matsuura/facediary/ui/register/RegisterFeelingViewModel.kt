package jp.matsuura.facediary.ui.register

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.extenstion.toBinary
import jp.matsuura.facediary.data.datasource.CacheDir
import jp.matsuura.facediary.usecase.CalculateEmotionUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class RegisterFeelingViewModel @Inject constructor(
    private val calculateEmotion: CalculateEmotionUseCase,
    private val cacheDir: CacheDir,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
            isButtonEnable = false,
            isRadioButtonEnabled = true,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    fun onRadioButtonClicked(isChecked: Boolean) {
        _uiState.value = _uiState.value.copy(isButtonEnable = isChecked)
    }

    fun onCheckBoxClicked(isChecked: Boolean) {
        _uiState.value = _uiState.value.copy(
            isButtonEnable = isChecked,
            isRadioButtonEnabled = !isChecked,
        )
    }

    data class UiState(
        val isProgressVisible: Boolean,
        val isButtonEnable: Boolean,
        val isRadioButtonEnabled: Boolean,
    )

    sealed class Event {
        object Success: Event()
        object Failure: Event()
    }
}