package jp.matsuura.facediary.common.extenstion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.AlertBinding

fun Context.showConfirm (
    @StringRes titleRes: Int,
    @StringRes messageRes: Int,
    @StringRes positiveButtonRes: Int?,
    @StringRes negativeButtonRes: Int?,
    onPositiveClick: ((AlertDialog) -> Unit),
    onNegativeClick: ((AlertDialog) -> Unit),
    isCancel: Boolean = false,
) {

    val binding = AlertBinding.inflate(LayoutInflater.from(this))
    binding.title.text = getString(titleRes)
    binding.message.text = getString(messageRes)

    if (positiveButtonRes == null) {
        binding.positiveButton.text = getString(R.string.ok)
    } else {
        binding.positiveButton.text = getString(positiveButtonRes)
    }

    if (negativeButtonRes == null) {
        binding.positiveButton.text = getString(R.string.cancel)
    } else {
        binding.negativeButton.text = getString(negativeButtonRes)
    }

    val dialog = MaterialAlertDialogBuilder(this)
        .setView(binding.root)
        .setCancelable(isCancel)
        .create()

    binding.positiveButton.setOnClickListener {
        onPositiveClick(dialog)
    }

    binding.negativeButton.setOnClickListener {
        onNegativeClick(dialog)
    }

    dialog.show()
}

fun Context.showMessage (
    @StringRes titleRes: Int,
    @StringRes messageRes: Int,
    @StringRes positiveButtonRes: Int = R.string.ok,
    onPositiveClick: ((AlertDialog) -> Unit),
    isCancel: Boolean = false,
) {

    val binding = AlertBinding.inflate(LayoutInflater.from(this))
    binding.title.text = getString(titleRes)
    binding.message.text = getString(messageRes)

    binding.positiveButton.text = getString(positiveButtonRes)

    binding.negativeButton.visibility = View.GONE

    val dialog = MaterialAlertDialogBuilder(this)
        .setView(binding.root)
        .setCancelable(isCancel)
        .create()

    binding.positiveButton.setOnClickListener {
        onPositiveClick(dialog)
    }

    dialog.show()
}