package jp.matsuura.facediary.ui.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentSinginBinding
import jp.matsuura.facediary.databinding.FragmentSingupBinding
import jp.matsuura.facediary.databinding.FragmentSingupSuccessBinding
import jp.matsuura.facediary.ui.signUp.SignUpViewModel
import kotlinx.coroutines.flow.onEach

/**
 * サインアップ成功画面
 */

@AndroidEntryPoint
class SignUpSuccessFragment: Fragment(R.layout.fragment_singup_success) {}