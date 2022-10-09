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
class ShootCameraViewModel @Inject constructor() : ViewModel() {

}