package jp.matsuura.facediary.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera.Face
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.common.extenstion.showConfirm
import jp.matsuura.facediary.common.extenstion.showMessage
import jp.matsuura.facediary.databinding.FragmentShootCameraBinding
import jp.matsuura.facediary.enums.FaceApiError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShootCameraFragment : Fragment(R.layout.fragment_shoot_camera) {

    private var _binding: FragmentShootCameraBinding? = null
    private val binding: FragmentShootCameraBinding get() = _binding!!

    private val viewModel: ShootCameraViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                takePicture()
            } else {
                requireContext().showConfirm(
                    titleRes = R.string.register_feeling_dialog_camera_error_title,
                    messageRes = R.string.register_feeling_dialog_camera_error_message,
                    positiveButtonRes = R.string.register_feeling_dialog_camera_error_positive_button,
                    negativeButtonRes = R.string.register_feeling_dialog_camera_error_negative_button,
                    onPositiveClick = { dialog ->
                        dialog.dismiss()
                        val intent = Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.parse("package:jp.matsuura.facediary")
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        startActivity(intent)
                    },
                    onNegativeClick = { dialog ->
                        dialog.dismiss()
                    },
                )
            }
        }

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            viewModel.onCameraFinish(bitmap = bitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShootCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                handleUiState(coroutineScope = this)
                handleEvent(coroutineScope = this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.shootButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    takePicture()
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }
    }

    private fun handleUiState(coroutineScope: CoroutineScope) {
        viewModel.uiState.onEach {
            binding.progressBar.isVisible = it.isProgressVisible
        }.launchIn(coroutineScope)
    }

    private fun handleEvent(coroutineScope: CoroutineScope) {
        viewModel.event.onEach {
        }.launchIn(coroutineScope)

        viewModel.faceApiEvent.onEach {
            when (it) {
                is ShootCameraViewModel.FaceApiEvent.Success -> {}
                is ShootCameraViewModel.FaceApiEvent.Failure -> {
                    when (it.error) {
                        FaceApiError.NO_ONE_EXISTED -> {
                            requireContext().showMessage(
                                titleRes = R.string.shoot_camera_dialog_title_no_people,
                                messageRes = R.string.shoot_camera_dialog_message_no_people,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        FaceApiError.MANY_PEOPLE_EXISTED -> {
                            requireContext().showMessage(
                                titleRes = R.string.shoot_camera_dialog_title_many_people,
                                messageRes = R.string.shoot_camera_dialog_message_many_people,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                    }
                }
                is ShootCameraViewModel.FaceApiEvent.NetworkError -> {
                    requireContext().showMessage(
                        titleRes = R.string.network_error_title,
                        messageRes = R.string.network_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                is ShootCameraViewModel.FaceApiEvent.UnknownError -> {
                    requireContext().showMessage(
                        titleRes = R.string.other_error_title,
                        messageRes = R.string.other_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
            }
        }.launchIn(coroutineScope)
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        takePhotoLauncher.launch(intent)
    }
}