package jp.matsuura.facediary.extenstions

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import jp.matsuura.facediary.databinding.AlertBinding

fun Context.showAlert (
    @StringRes titleRes: Int,
    @StringRes messageRes: Int,
    @StringRes positiveButtonRes: Int,
    @StringRes negativeButtonRes: Int,
    onPositiveClick: ((AlertDialog) -> Unit),
    onNegativeClick: ((AlertDialog) -> Unit),
    isCancel: Boolean = false,
) {

    val binding = AlertBinding.inflate(LayoutInflater.from(this))
    binding.title.text = getString(titleRes)
    binding.message.text = getString(messageRes)
    binding.positiveButton.text = getString(positiveButtonRes)
    binding.negativeButton.text = getString(negativeButtonRes)

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