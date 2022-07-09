package jp.matsuura.facediary.ui.signUp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.repositories.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {
}