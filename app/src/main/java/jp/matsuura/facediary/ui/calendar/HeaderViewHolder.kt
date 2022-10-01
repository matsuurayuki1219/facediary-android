package jp.matsuura.facediary.ui.calendar

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.ItemHeaderBinding
import jp.matsuura.facediary.common.extenstion.BaseViewHolder

class HeaderViewHolder(private val binding: ItemHeaderBinding): BaseViewHolder(binding.root) {

    fun bind(youbi: String) {

        val context = binding.root.context

        @ColorRes
        val color = when (youbi) {
            "Sun" -> R.color.red50
            "Sat" -> R.color.blue50
            else -> R.color.black
        }

        binding.youbi.text = youbi
        binding.youbi.setTextColor(ContextCompat.getColor(context, color))

    }

}